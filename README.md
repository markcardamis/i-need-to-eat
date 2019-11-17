# I need to eat
An API to retrieve a list of recipes based on the ingredients in your fridge.

## Getting Started
### Using Docker
* Open terminal and run ```docker --version``` to make sure you have docker installed otherwise download Docker Desktop from https://hub.docker.com/search?q=desktop&type=edition&offering=community 
* Run the image as a docker container using ```docker run -p 8080:8080 -t markcardamis/lunchapp:1.0.0```
* Make a GET request to ```http://localhost:8080/api/v1/lunch```

### Using Gradle
* Clone the repository using Git:  
```git clone https://github.com/markcardamis/i-need-to-eat.git```
* Change into the directory using ```cd i-need-to-eat```
* Make sure the tests pass ```./gradlew clean test```
* Run the program using ```./gradlew bootRun```
* Make a GET request to ```http://localhost:8080/api/v1/lunch```

## Project Structure
### Build Gradle
The dependencies for the project are installed using Gradle with the build.gradle file below. 
A few of the Spring starter packages were implemented along with Jackson for Json annotations and Lombok for boilerplate code. An in memory database H2 was used in order to benefit from some of the JPA queries and as a provision to easily add an external database in the future.

```
buildscript {
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.0.RELEASE")
    classpath "se.transmode.gradle:gradle-docker:1.2"
  }
}

plugins {
	id 'org.springframework.boot' version '2.2.0.RELEASE'
	id 'io.spring.dependency-management' version '1.0.8.RELEASE'
	id 'java'
}

apply plugin: 'java'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'
apply plugin: "docker"

group = 'markcardamis'
version = '1.0.0'

sourceCompatibility = '1.8'

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-data-rest'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	implementation 'com.fasterxml.jackson.core:jackson-core:2.10.0'
	implementation 'com.fasterxml.jackson.core:jackson-databind:2.10.0'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

test {
	useJUnitPlatform()

	testLogging {
		events "passed", "skipped", "failed"
	}
}

task buildDocker(type: Docker, dependsOn: build) {
  push = true
  applicationName = jar.baseName
  dockerfile = file('Dockerfile')
  doFirst {
    copy {
      from jar
      into stageDir
    }
  }
}
```


### Directory Structure
Using Domain Driven Design the following folder structure is a skeleton of the project implementation.
```
└── src
    └── main
        └── java
            └── com.majoapps.lunchapp
                └── business
                    └── domain
						└── IngredientDto.java
						└── IngredientDtoWrapper.java
						└── LunchResponse.java
						└── RecipeDto.java
						└── RecipeDtoWrapper.java
                    └── service
						└── IngredientService.java
						└── LunchService.java
						└── RecipeService.java
						└── RestTemplateService.java
                └── data
                    └── entity
						└── Ingredient.java
						└── Lunch.java
						└── Recipe.java
                    └── repository
						└── IngredientRepository.java
						└── LunchRespository.java
						└── RecipeRepository.java
                └── exception
					└── ResourceNotFoundException.java
                └── web
					└── LunchServiceController.java
				└── LunchappApplication.java
				└── MyRunner.java
```
## Controllers
The `LunchServiceController` sets up the HTTP route and sets the endpoint. It delegates its work to the `LunchService` and waits for a LunchResponse object which has the list of options for lunch in a JSON format.

```
@RestController
@RequestMapping(value="/api/v1/lunch")
public class LunchServiceController {

    private final LunchService lunchService;

    @Autowired
    private LunchServiceController(LunchService lunchService){
        this.lunchService = lunchService;
    }
    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<LunchResponse> get() {
        try {
            LunchResponse lunchResponse = lunchService.get();
            if (lunchResponse == null || lunchResponse.getRecipes().isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(lunchResponse);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
```
## Services
### LunchService
The `LunchService` contains the business logic used to determine what are the lunch options for the day. The @Service annotation to provide the interface with: 
* `LunchRepository` - DAO for interacting with the database table LUNCH
* `RestTemplateService` - a HTTP helper class to retrieve both the recipes and ingredients data from the remote server
* `RecipeService` - exposes all recipe database methods consumed by this service along with the business logic for updating or creating new Recipe records.
* `IngredientService` - exposes all the ingredients database methods consumed by this service along with the business logic for updating or creating new Ingredient records.

The business logic consists of the following steps:
1. Fetch and save the recipes and ingredients json response into the database.
1. Create a Lunch HashMap using ingredients in the database, filtering out those past their use-by date.
1. Compare the created LunchMap with the original ingredient list to see which recipes have all the ingredients.
1. Save the complete recipes in a LUNCH table and along with the oldest ingredient best-before date.
1. Send back a List to the RestController using JPA's OrderBy on the best-before column, listing the recipes with the freshest ingredients first.

```
@Service
public class LunchService {

    private final LunchRepository lunchRepository;
    private final RestTemplateService restTemplateService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @Autowired
    public LunchService(LunchRepository lunchRepository, RestTemplateService restTemplateService, 
                RecipeService recipeService, IngredientService ingredientService) {
        this.lunchRepository = lunchRepository;
        this.restTemplateService = restTemplateService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    public LunchResponse get() throws Exception {
        //clear lunch table database entries so duplicates do not appear, use soft delete in future
        lunchRepository.deleteAll();

        saveRecipeListToDatabase(restTemplateService.getRecipes());
        saveIngredientsListToDatabase(restTemplateService.getIngredients());

        // create a lunchMap (HashMap) from the recipe and ingredient database tables
        Map<String, List<Ingredient>> lunchMap = createLunchMapWithGoodIngredients();
        
        // save the recipes which are valid options in the lunch table
        saveRecipesWithCompleteIngredientsToDatabase(lunchMap);

        // order the lunch options by Best Before
        List<Lunch> lunches = lunchRepository.findAllByOrderByBestBeforeDesc();        

        // send back the response using the LunchResponse model
        LunchResponse lunchResponse = new LunchResponse();
        lunchResponse.setRecipes(lunches);

        return lunchResponse;
    }

	// other methods skipped for readability
}
```

### RestTemplateService
The `RestTemplateService` uses RestTemplate to make the HTTP requests to retrieve the JSON objects from the remote web servers. Using the `restTemplate.getForObject()` method, we are able to receive the body of the HTTP call only in the response.

```
@Service
public class RestTemplateService {

    private final String INGREDIENT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
    private final String RECIPE_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
    
    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<RecipeDto> getRecipes() throws Exception {
        return restTemplate.getForObject(RECIPE_URI, RecipeDtoWrapper.class).getRecipes();
    }

    public List<IngredientDto> getIngredients() throws Exception {
        return restTemplate.getForObject(INGREDIENT_URI, IngredientDtoWrapper.class).getIngredients();
    }

}
```
### IngredientService
The `IngredientService` handles the following operations:
* Data conversion from IngredientDto to the Ingredient Entity. 
* Implements the `IngredientRepository` to handle the database interactions within the INGREDIENT table.
* Handles logic to either update an existing record or create a new one.


```
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository = ingredientRepository;
    }

    Ingredient saveIngredient(@NonNull IngredientDto ingredient) {
        List<Ingredient> ingredientResponse = ingredientRepository
            .findByTitle(ingredient.getTitle());
        Ingredient ingredientEntity = new Ingredient();
        if (ingredientResponse.isEmpty()) { //add new ingredient as it doesn't exist 
            ingredientEntity.setTitle(ingredient.getTitle());
            ingredientEntity.setBestBefore(ingredient.getBestBefore());
            ingredientEntity.setUseBy(ingredient.getUseBy());
            ingredientRepository.save(ingredientEntity);
        } else { //update existing using index 0 as there should only be one type
            ingredientEntity.setId(ingredientResponse.get(0).getId());
            ingredientEntity.setTitle(ingredientResponse.get(0).getTitle());
            ingredientEntity.setBestBefore(ingredient.getBestBefore());
            ingredientEntity.setUseBy(ingredient.getUseBy());
            ingredientRepository.save(ingredientEntity);
        }
        return ingredientEntity;
    }

    List<Ingredient> findByUseByAfter(LocalDate localDate) {
        return ingredientRepository.findByUseByAfter(localDate);
    }

}
```
### Data Transfer Objects (DTO)
We use Data Transfer Objects to handle the conversion between the JSON object and the internal entities.
An `IngredientDto` was used to store to incoming json from the remote web server. The `IngredientService` could then transform the external model into the entity model. The annotation `@JsonProperty` was used to keep Google's Java Style Guide with reference to variable names. `@Data` annotation is for Lombok to auto-generate some boiler-plate code like getters/setters.

```
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {

    @JsonProperty("title") private String title;
    @JsonProperty("best-before") private LocalDate bestBefore;
    @JsonProperty("use-by") private LocalDate useBy;
    
}
```
A `IngredientDtoWrapper` was created as there was a root wrapper name in the JSON response object. The jackson annotation `@JsonRootName` was set to handle this.

```
@JsonRootName(value = "ingredients") //root Wrapper name
public class IngredientDtoWrapper {

    private List<IngredientDto> ingredients;

    public IngredientDtoWrapper() {
        ingredients = new ArrayList<>();
    }

}
```
## Models




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
plugins {
	id 'org.springframework.boot' version '2.2.0.RELEASE'
	id 'io.spring.dependency-management' version '1.0.8.RELEASE'
	id 'java'
}

group = 'com.majoapps'
version = '0.0.1-SNAPSHOT'
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
@Data
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














@Data annotation is for Lombok to auto-generate some boiler-plate code like getters/setters.
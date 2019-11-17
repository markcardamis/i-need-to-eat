package com.majoapps.lunchapp.business.service;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Lunch;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.LunchRepository;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("test")
class LunchServiceTest {
    private final String INGREDIENT_JSON_FILENAME = "src/test/java/com/majoapps/lunchapp/ingredients.json";
    private final String RECIPE_JSON_FILENAME = "src/test/java/com/majoapps/lunchapp/recipes.json";
    private ObjectMapper objectMapper;
    private final String todayDate = "2019-11-16";

    private final LunchRepository lunchRepository;
    private final RestTemplateService restTemplateService;
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @Autowired
    public LunchServiceTest(LunchRepository lunchRepository, RestTemplateService restTemplateService, 
                RecipeService recipeService, IngredientService ingredientService) {
        this.lunchRepository = lunchRepository;
        this.restTemplateService = restTemplateService;
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule(); //set LocalTime text format in JSON
        javaTimeModule.addDeserializer(LocalDate.class, 
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(javaTimeModule);
    }

    @AfterEach
    void teardown() {
        recipeService.getRecipeRepository().deleteAll(); // clear any data between tests
        ingredientService.getIngredientRepository().deleteAll();
        lunchRepository.deleteAll();
    }


    @Test
    @DisplayName("Save recipe list to database and verify with JSON example")
    void saveRecipeListToDatabase() throws Exception {
        // Get recipeDtos from local file
        RecipeDtoWrapper recipeDtoWrapper = objectMapper.readValue
                (new File(RECIPE_JSON_FILENAME), RecipeDtoWrapper.class);
        List<RecipeDto> recipeDtosFile =  recipeDtoWrapper.getRecipes();

        // Get recipeDtos from remote server
        List<RecipeDto> recipeDtos = restTemplateService.getRecipes();

        // Iterate through the recipe list and save to the database
        recipeDtos.forEach(recipeService::saveRecipe);

        // Verify that each recipe title from the file exists in the database
        for (RecipeDto recipeDto : recipeDtosFile) {
            List<Recipe> recipeListResponse = recipeService.getRecipeRepository().
                findByTitle(recipeDto.getTitle());
            assertFalse(recipeListResponse.isEmpty());
        }

        // Verify that each ingredient title from the file exists in the database
        for (RecipeDto recipeDto : recipeDtosFile) {
            for (String ingredients: recipeDto.getIngredients()) {
                List<Recipe> recipeListResponse = recipeService.getRecipeRepository().
                    findByTitleAndIngredient(recipeDto.getTitle(), ingredients);
                assertFalse(recipeListResponse.isEmpty());
            }
        }
    }

    @Test
    @DisplayName("Save ingredient list to database and verify with JSON example")
    void saveIngredientsListToDatabase() throws Exception {
        // Get ingredientDtos from local file
        IngredientDtoWrapper ingredientDtoWrapper = objectMapper.readValue
                (new File(INGREDIENT_JSON_FILENAME), IngredientDtoWrapper.class);
        List<IngredientDto> ingredientDtosFile =  ingredientDtoWrapper.getIngredients();

        // Get ingredientDtos from remote server
        List<IngredientDto> ingredientDtos = restTemplateService.getIngredients();

        // Iterate through the ingredient list and save to the database
        ingredientDtos.forEach(ingredientService::saveIngredient);

        // Verify that each ingredient title from the file exists in the database
        for (IngredientDto ingredientDto : ingredientDtosFile) {
            List<Ingredient> ingredientListResponse = ingredientService.getIngredientRepository().
                findByTitle(ingredientDto.getTitle());
            assertFalse(ingredientListResponse.isEmpty());
        }
    }

    @Test
    @DisplayName("Create a HashMap lunch list with good ingredients and verify with JSON example")
    void createLunchMapWithGoodIngredients() throws Exception {
    
        saveMockRecipeAndIngredientListToDatabase(); //use example Json to create database tables
        
        //return a Map with ingredients still within use-by using setting mocked today date
        Map<String, List<Ingredient>> lunchMap = createLunchHashMap();
        
        /**
        {
            Hotdog=[
                Ingredient(id=35, title=Ketchup, bestBefore=2019-11-13, useBy=2019-11-18), 
                Ingredient(id=36, title=Mustard, bestBefore=2019-11-13, useBy=2019-11-18)
            ], 
            Ham and Cheese Toastie=[
                Ingredient(id=26, title=Ham, bestBefore=2019-11-13, useBy=2019-11-18), 
                Ingredient(id=27, title=Cheese, bestBefore=2019-11-13, useBy=2019-11-18), 
                Ingredient(id=28, title=Bread, bestBefore=2019-11-13, useBy=2019-11-18), 
                Ingredient(id=29, title=Butter, bestBefore=2019-11-13, useBy=2019-11-18)
            ], 
            Fry-up=[
                Ingredient(id=28, title=Bread, bestBefore=2019-11-13, useBy=2019-11-18)
            ]
        }
        **/

        assertTrue(lunchMap.containsKey("Hotdog"));
        assertTrue(lunchMap.get("Hotdog").size() == 2);
        assertTrue(lunchMap.containsKey("Ham and Cheese Toastie"));
        assertTrue(lunchMap.get("Ham and Cheese Toastie").size() == 4);
        assertTrue(lunchMap.containsKey("Fry-up"));
        assertTrue(lunchMap.get("Fry-up").size() == 1);

    }

    @Test
    @DisplayName("Save recipes to lunch table that have complete and usable ingredients")
    void saveRecipesWithCompleteIngredientsToDatabase() {
        
        saveMockRecipeAndIngredientListToDatabase(); //use example Json to create database tables

        //return a Map with ingredients still within use-by using setting mocked today date
        Map<String, List<Ingredient>> lunchMap = createLunchHashMap();
        
        //run through the lunch HashMap and check we have all the ingredients
        for (String recipeTitle : lunchMap.keySet()) {
            List<Recipe> recipes = recipeService.findByTitle(recipeTitle);
            //check if we have all the ingredients in the recipe
            if (lunchMap.get(recipeTitle).size() == recipes.size()) {
                Lunch lunchEntity = new Lunch();
                lunchEntity.setTitle(recipeTitle);
                if (lunchEntity.getBestBefore() == null) {
                    lunchEntity.setBestBefore(lunchMap.get(recipeTitle).get(0).getBestBefore());
                }
                for (Ingredient ingredientResponse : lunchMap.get(recipeTitle)) {
                    if (lunchEntity.getBestBefore().isAfter(ingredientResponse.getBestBefore())) {
                        lunchEntity.setBestBefore(ingredientResponse.getBestBefore());
                    }
                }
                //save the recipe in the lunch table once it is an option
                lunchRepository.save(lunchEntity);
            }       
        }

        /** 
        [Lunch(id=42, title=Ham and Cheese Toastie, bestBefore=2019-11-13)]
        **/

        assertEquals("Ham and Cheese Toastie", lunchRepository.findAll().get(0).getTitle());
    }

    @Test
    void findAllByOrderByBestBefore() throws Exception {
        Lunch lunchEntityOld = new Lunch();
        lunchEntityOld.setTitle("Recipe Old");
        lunchEntityOld.setBestBefore(LocalDate.parse("2019-11-15"));
        lunchRepository.save(lunchEntityOld);
        Lunch lunchEntityNew = new Lunch();
        lunchEntityNew.setTitle("Recipe New");
        lunchEntityNew.setBestBefore(LocalDate.parse("2019-11-18"));
        lunchRepository.save(lunchEntityNew);
        Lunch lunchEntity = new Lunch();
        lunchEntity.setTitle("Recipe Neutral");
        lunchEntity.setBestBefore(LocalDate.parse("2019-11-16"));
        lunchRepository.save(lunchEntity);
                
        List<Lunch> lunchesByNew = lunchRepository.findAllByOrderByBestBeforeDesc();
        assertEquals(lunchesByNew.get(0).getTitle(), lunchEntityNew.getTitle());

        List<Lunch> lunchesByOld = lunchRepository.findAllByOrderByBestBeforeAsc();    
        assertEquals(lunchesByOld.get(0).getTitle(), lunchEntityOld.getTitle());
    }


    private void saveMockRecipeAndIngredientListToDatabase() {
        try {
            // Get recipeDtos from local file and save to the database
            RecipeDtoWrapper recipeDtoWrapper = objectMapper.readValue
                (new File(RECIPE_JSON_FILENAME), RecipeDtoWrapper.class);
            List<RecipeDto> recipeDtosFile =  recipeDtoWrapper.getRecipes();
            recipeDtosFile.forEach(recipeService::saveRecipe);

            // Get ingredientDtos from local file and save to the database
            IngredientDtoWrapper ingredientDtoWrapper = objectMapper.readValue
                    (new File(INGREDIENT_JSON_FILENAME), IngredientDtoWrapper.class);
            List<IngredientDto> ingredientDtosFile =  ingredientDtoWrapper.getIngredients();
            ingredientDtosFile.forEach(ingredientService::saveIngredient);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    private Map<String, List<Ingredient>> createLunchHashMap() {
        //create a hashMap of recipes with good ingredients from the database using a MockDate
        Map<String, List<Ingredient>> lunchMap = new HashMap<>();

        List<Ingredient> ingredients = ingredientService.findByUseByAfter(LocalDate.parse(todayDate));
        for (Ingredient ingredient : ingredients) {
            List<Recipe> recipes = recipeService.findByIngredient(ingredient.getTitle());
            recipes.forEach(recipe -> {
                List<Ingredient> ingredientList = lunchMap.get(recipe.getTitle());
                if (ingredientList == null)
                {
                    ingredientList = new ArrayList<>();
                    lunchMap.put(recipe.getTitle(), ingredientList);
                }
                ingredientList.add(ingredient);
            });
        }
        return lunchMap;
    }

}

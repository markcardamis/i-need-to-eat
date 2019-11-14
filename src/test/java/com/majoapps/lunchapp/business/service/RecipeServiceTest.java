package com.majoapps.lunchapp.business.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
class RecipeServiceTest {
    private final String RECIPE_JSON_FILENAME = "src/test/java/com/majoapps/lunchapp/recipes.json";
    private ObjectMapper objectMapper;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(javaTimeModule);
    }

    @Test
    @DisplayName("Test save recipe to repository")
    void saveRecipe() throws Exception {
        RecipeDtoWrapper recipeDtoWrapper = objectMapper.readValue
                (new File(RECIPE_JSON_FILENAME), RecipeDtoWrapper.class);
        List<RecipeDto> recipeDtos =  recipeDtoWrapper.getRecipes();

        when(recipeRepository.save(any(Recipe.class))).thenReturn(new Recipe());

        if (recipeDtos.size() > 0) {  //only proceed if loaded sample local json file
            Recipe recipeCreated = recipeService.saveRecipe(recipeDtos.get(0));
            assertEquals(recipeCreated.getTitle(), recipeDtos.get(0).getTitle());
        } else {
            assertTrue(false);
        }
    }

    @Test
    void findByTitle() throws Exception {
        List<Recipe> recipeArrayList = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setTitle("HotDog");
        recipeArrayList.add(recipe);
        
        when(recipeRepository.findByTitle(any())).thenReturn(recipeArrayList);

        List<Recipe> recipes = recipeService.findByTitle("HotDog");
        assertTrue(recipes.size() == 1);
        assertEquals(recipes.get(0).getTitle(), recipe.getTitle());
    }

    @Test
    void findByIngredient() throws Exception {
        List<Recipe> recipeArrayList = new ArrayList<>();
        Recipe recipe = new Recipe();
        recipe.setId(1);
        recipe.setTitle("HotDog");
        recipe.setIngredient("Bread");
        recipeArrayList.add(recipe);
        
        when(recipeRepository.findByIngredient(any())).thenReturn(recipeArrayList);

        List<Recipe> recipes = recipeService.findByIngredient("Bread");
        assertTrue(recipes.size() == 1);
        assertEquals(recipes.get(0).getIngredient(), recipe.getIngredient());
    }
}

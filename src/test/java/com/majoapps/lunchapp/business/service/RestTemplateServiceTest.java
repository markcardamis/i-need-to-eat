package com.majoapps.lunchapp.business.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
class RestTemplateServiceTest {

    private final String INGREDIENT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
    private final String RECIPE_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
    private final String INGREDIENT_JSON = "src/test/java/com/majoapps/lunchapp/ingredients.json";
    private final String RECIPE_JSON = "src/test/java/com/majoapps/lunchapp/recipes.json";
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;
 
    @InjectMocks
    private RestTemplateService restTemplateService;// = new RestTemplateService(restTemplate);
    
    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();  //set LocalTime text format in JSON
        javaTimeModule.addDeserializer(LocalDate.class, 
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(javaTimeModule);
    }

    @Test
    void getRecipes() throws Exception {
        RecipeDtoWrapper recipeDtoWrapper = objectMapper.readValue
                (new File(RECIPE_JSON), RecipeDtoWrapper.class);
        List<RecipeDto> recipeDto =  recipeDtoWrapper.getRecipes();

        when(restTemplate.getForObject(RECIPE_URI, RecipeDtoWrapper.class)).
            thenReturn(recipeDtoWrapper);

        if (recipeDto.size() > 0) { //only proceed if loaded sample local json file
            List<RecipeDto> recipeCreated = restTemplateService.getRecipes();
            assertTrue(recipeCreated.equals(recipeDto));
        } else {
            assertTrue(false);
        }
    }

    @Test
    void getIngredients() throws Exception {
        IngredientDtoWrapper ingredientDtoWrapper = objectMapper.readValue
            (new File(INGREDIENT_JSON), IngredientDtoWrapper.class);
        List<IngredientDto> ingredientDto =  ingredientDtoWrapper.getIngredients();

        when(restTemplate.getForObject(INGREDIENT_URI, IngredientDtoWrapper.class)).
            thenReturn(ingredientDtoWrapper);

        if (ingredientDto.size() > 0) { //only proceed if loaded sample local json file
            List<IngredientDto> ingredientCreated = restTemplateService.getIngredients();
            assertTrue(ingredientCreated.equals(ingredientDto));
        } else {
            assertTrue(false);
        }
    }
}
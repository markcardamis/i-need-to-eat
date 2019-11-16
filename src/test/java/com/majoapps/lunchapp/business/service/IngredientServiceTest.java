package com.majoapps.lunchapp.business.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
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
class IngredientServiceTest {
    private final String INGREDIENT_JSON = "src/test/java/com/majoapps/lunchapp/ingredients.json";
    private ObjectMapper objectMapper;
    
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private IngredientService ingredientService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule(); //set LocalTime text format in JSON
        javaTimeModule.addDeserializer(LocalDate.class, 
            new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(javaTimeModule);
    }
    
    @Test
    @DisplayName("MockTest save ingredient to repository")
    void saveIngredient() throws Exception {
        IngredientDtoWrapper ingredientDtoWrapper = objectMapper.readValue
                (new File(INGREDIENT_JSON), IngredientDtoWrapper.class);
        List<IngredientDto> ingredientDtos =  ingredientDtoWrapper.getIngredients();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(new Ingredient());

        if (ingredientDtos.size() > 0) {  //only proceed if loaded sample local json file
            Ingredient ingredientCreated = ingredientService.saveIngredient(ingredientDtos.get(0));
            assertEquals(ingredientCreated.getTitle(), ingredientDtos.get(0).getTitle());
        } else {
            assertTrue(false);
        }
    }

    @Test
    @DisplayName("MockTest retrieving ingredients by after use-by")
    void findByUseByAfter() throws Exception {

        // set an ingredient with good use-by date
        List<Ingredient> ingredientsArrayList = new ArrayList<>();
        Ingredient ingredientGood = new Ingredient();
        ingredientGood.setId(1);
        ingredientGood.setTitle("Ham");
        ingredientGood.setBestBefore(LocalDate.now().minusDays(2));
        ingredientGood.setUseBy(LocalDate.now().plusDays(2));
        ingredientsArrayList.add(ingredientGood);
        
        when(ingredientRepository.findByUseByAfter(any())).thenReturn(ingredientsArrayList);

        List<Ingredient> ingredients = ingredientService.findByUseByAfter(LocalDate.now());
        assertTrue(ingredients.size() == 1);
        assertEquals(ingredients.get(0).getTitle(), ingredientGood.getTitle());
    }

}

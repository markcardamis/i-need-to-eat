package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import com.majoapps.lunchapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import lombok.Data;

@Data
@Service
public class IngredientClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    RecipeRepository recipeRepository;


    public List<IngredientDto> getIngredientsRestTemplate() throws Exception {
        return restTemplate.getForObject(ROOT_URI, IngredientDtoWrapper.class).getIngredients();
    }


    public List<IngredientDto> saveIngredients() throws Exception {
        List<IngredientDto> ingredients = getIngredientsRestTemplate();
        
        if (ingredients == null || ingredients.isEmpty()) {
            throw new ResourceNotFoundException("Cannot retrieve any ingredients");
        } else {
            // iterate and save each object if it doesn't already exist
            ingredients.forEach(ingredient -> {
                List<Ingredient> ingredientResponse = ingredientRepository.findByTitle(ingredient.getTitle());
                Ingredient ingredientEntity = new Ingredient();
                if (ingredientResponse.isEmpty()) { //add new ingredient as it doesn't exist 
                    ingredientEntity.setTitle(ingredient.getTitle());
                    ingredientEntity.setBestBefore(ingredient.getBestBefore());
                    ingredientEntity.setUseBy(ingredient.getUseBy());
                    ingredientRepository.save(ingredientEntity);
                } else for (Ingredient ingredientTemp : ingredientResponse) { // update Dates on existing items
                    ingredientEntity.setId(ingredientTemp.getId());
                    ingredientEntity.setTitle(ingredientTemp.getTitle());
                    ingredientEntity.setRecipe(ingredientTemp.getRecipe());
                    ingredientEntity.setBestBefore(ingredient.getBestBefore());
                    ingredientEntity.setUseBy(ingredient.getUseBy());
                    ingredientRepository.save(ingredientEntity);
                }
            });
        }
        return ingredients;
    }   

}

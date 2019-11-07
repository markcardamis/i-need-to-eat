package com.majoapps.lunchapp.business.service;

import java.util.List;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
@Service
public class IngredientClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    IngredientRepository ingredientRepository;

    // public IngredientDtoWrapper get() throws Exception {
    //     return restTemplate.getForObject(ROOT_URI, IngredientDtoWrapper.class);
    // }

    public List<IngredientDto> getIngredients() throws Exception {
        return restTemplate.getForObject(ROOT_URI, IngredientDtoWrapper.class).getIngredients();
    }

    public List<IngredientDto> saveIngredients() {
        List<IngredientDto> ingredients = restTemplate.getForObject(ROOT_URI, IngredientDtoWrapper.class)
                .getIngredients();
        if (ingredients == null || ingredients.size() == 0) {
            throw new ResourceNotFoundException("Cannot retrieve any ingredients");
        } else {
            // iterate and save each object if it doesn't already exist
            ingredients.forEach(ingredient -> {
                Ingredient ingredientEntity = new Ingredient();
                ingredientEntity.setTitle(ingredient.getTitle());
                ingredientEntity.setBestBefore(ingredient.getBestBefore());
                ingredientEntity.setUseBy(ingredient.getUseBy());
                ingredientRepository.save(ingredientEntity);
            });
        }
        return ingredients;
    }   

}

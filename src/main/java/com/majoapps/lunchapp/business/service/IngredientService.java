package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
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

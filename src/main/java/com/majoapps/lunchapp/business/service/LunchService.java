package com.majoapps.lunchapp.business.service;

import java.time.LocalDate;
import java.util.List;

import com.majoapps.lunchapp.business.domain.LunchOptions;
import com.majoapps.lunchapp.data.entity.Ingredient;
import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.IngredientRepository;
import com.majoapps.lunchapp.data.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

@Data
@Service
public class LunchService {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    public Iterable<Recipe> getRecipes() {
        return null;
    }

    public Iterable<Recipe> getRecipesWithIngredients() throws Exception {
        return this.recipeRepository.findAll();
    }

    public List<LunchOptions> get() { 

        Iterable<Ingredient> ingredients = ingredientRepository.findByUseByAfter(LocalDate.now());
        


        return null;
    }

    

}

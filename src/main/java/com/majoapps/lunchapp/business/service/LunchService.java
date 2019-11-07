package com.majoapps.lunchapp.business.service;

import java.util.List;

import com.majoapps.lunchapp.data.entity.Recipe;
import com.majoapps.lunchapp.data.repository.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

@Data
@Service
public class LunchService {

    @Autowired
    RecipeRepository recipeRepository;

    public Iterable<Recipe> getRecipes() {
        return null;
    }

    public Iterable<Recipe> getRecipesWithIngredients() throws Exception {
        return recipeRepository.findAll();
    }

    

}

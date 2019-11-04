package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeResponse;

public interface IRecipeService {
    RecipeResponse getRecipes() throws Exception;
}

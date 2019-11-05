package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeList;

public interface RecipeClientService {
    RecipeList getRecipes() throws Exception;
}

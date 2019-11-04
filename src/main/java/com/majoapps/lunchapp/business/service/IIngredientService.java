package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientResponse;

public interface IIngredientService {
    IngredientResponse getIngredients() throws Exception;
}

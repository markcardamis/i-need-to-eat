package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientList;

public interface IngredientClientService {
    IngredientList getIngredients() throws Exception;
}

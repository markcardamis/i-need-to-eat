package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "ingredients") //root Wrapper name
public class IngredientWrapper {
    
    private List<Ingredient> ingredients;

    public IngredientWrapper() {
        ingredients = new ArrayList<>();
    }

}
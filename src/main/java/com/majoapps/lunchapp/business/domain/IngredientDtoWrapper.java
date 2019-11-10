package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "ingredients") //root Wrapper name
public class IngredientDtoWrapper {

    private List<IngredientDto> ingredients;

    public IngredientDtoWrapper() {
        ingredients = new ArrayList<>();
    }

}
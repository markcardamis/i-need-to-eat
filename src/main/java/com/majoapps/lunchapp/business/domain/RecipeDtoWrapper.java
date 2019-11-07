package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "recipes") //root Wrapper name
public class RecipeDtoWrapper {

    private List<RecipeDto> recipes;

    public RecipeDtoWrapper() {
        recipes = new ArrayList<>();
    }

}

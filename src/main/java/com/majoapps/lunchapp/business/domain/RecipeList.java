package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "recipes") //root Wrapper name
public class RecipeList {

    private List<Recipe> recipes;

    private static class Recipe {
        @JsonProperty("title") private String title;
        @JsonProperty("ingredients") private List<String> ingredients;
    }    

}

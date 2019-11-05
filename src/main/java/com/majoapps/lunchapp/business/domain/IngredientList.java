package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "ingredients") //root Wrapper name
public class IngredientList {
    
    private List<Ingredient> ingredients;

    private static class Ingredient {
        @JsonProperty("title") private String title;
        @JsonProperty("best-before") private String bestBefore;
        @JsonProperty("use-by") private String useBy;
    }

}
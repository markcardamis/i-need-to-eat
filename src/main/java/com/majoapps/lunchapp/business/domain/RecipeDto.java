package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecipeDto {

        @JsonProperty("title") private String title;
        @JsonProperty("ingredients") private List<String> ingredients;

}

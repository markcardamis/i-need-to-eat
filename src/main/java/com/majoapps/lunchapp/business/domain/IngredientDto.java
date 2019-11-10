package com.majoapps.lunchapp.business.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IngredientDto {

    @JsonProperty("title") private String title;
    @JsonProperty("best-before") private LocalDate bestBefore;
    @JsonProperty("use-by") private LocalDate useBy;
    
}
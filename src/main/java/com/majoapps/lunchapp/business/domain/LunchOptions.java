package com.majoapps.lunchapp.business.domain;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LunchOptions {
    private Integer recipeId;
    private Integer ingredientId;
    private String title;
    private LocalDate bestBefore;
    private LocalDate useBy;
    private String ingredient;
}

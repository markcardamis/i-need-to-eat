package com.majoapps.lunchapp.business.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class RecipeResponse {

    @SerializedName("title") private String title;
    @SerializedName("ingredients") private List<IngredientResponse> ingredients;

}

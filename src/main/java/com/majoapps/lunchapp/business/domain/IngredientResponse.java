package com.majoapps.lunchapp.business.domain;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import lombok.Data;

@Data
@SuppressWarnings("unused")
public class IngredientResponse {

    @SerializedName("title") private String address;
    @SerializedName("best-before") private Date bestBefore;
    @SerializedName("use-by") private Date useBy;

}


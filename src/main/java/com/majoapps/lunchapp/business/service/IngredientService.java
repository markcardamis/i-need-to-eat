package com.majoapps.lunchapp.business.service;

import com.google.gson.Gson;
import com.majoapps.lunchapp.business.domain.IngredientResponse;
import com.majoapps.lunchapp.utils.HttpServiceHelper;
import com.majoapps.lunchapp.utils.HttpMethod;
import com.majoapps.lunchapp.utils.IHttpServiceHelper;

public class IngredientService implements IIngredientService{

    private IHttpServiceHelper HttpServiceHelper;

    public IngredientService() {
        HttpServiceHelper = new HttpServiceHelper();
    }

    @Override
    public IngredientResponse getIngredients() throws Exception {
            String url = "https://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
            String responseJson = HttpServiceHelper.callHttpService(url, HttpMethod.GET, "");
            Gson gson = new Gson();
            return gson.fromJson(responseJson, IngredientResponse.class);
    }

}

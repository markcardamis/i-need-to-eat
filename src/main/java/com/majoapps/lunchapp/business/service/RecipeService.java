package com.majoapps.lunchapp.business.service;

import com.google.gson.Gson;
import com.majoapps.lunchapp.business.domain.RecipeResponse;
import com.majoapps.lunchapp.utils.HttpMethod;
import com.majoapps.lunchapp.utils.HttpServiceHelper;
import com.majoapps.lunchapp.utils.IHttpServiceHelper;

public class RecipeService implements IRecipeService {

    private IHttpServiceHelper HttpServiceHelper;

    public RecipeService() {
        HttpServiceHelper = new HttpServiceHelper();
    }

    @Override
    public RecipeResponse getRecipes() throws Exception {
        String url = "https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
        String responseJson = HttpServiceHelper.callHttpService(url, HttpMethod.GET, "");
        Gson gson = new Gson();
        return gson.fromJson(responseJson, RecipeResponse.class);
    }

}

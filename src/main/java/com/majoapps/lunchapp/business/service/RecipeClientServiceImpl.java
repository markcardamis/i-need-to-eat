package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
public class RecipeClientServiceImpl implements RecipeClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";

    @Autowired
    RestTemplate restTemplate;

    public RecipeList getRecipes() throws Exception {
        return restTemplate.getForObject(ROOT_URI, RecipeList.class);
    }

}

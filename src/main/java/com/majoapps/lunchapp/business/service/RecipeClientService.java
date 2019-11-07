package com.majoapps.lunchapp.business.service;

import java.util.List;

import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
@Service
public class RecipeClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";

    @Autowired
    RestTemplate restTemplate;

    public List<RecipeDto> getRecipes() throws Exception {
        return restTemplate.getForObject(ROOT_URI, RecipeDtoWrapper.class).getRecipes();
    }

}

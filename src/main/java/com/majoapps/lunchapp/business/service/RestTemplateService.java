package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.IngredientDtoWrapper;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.domain.RecipeDtoWrapper;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Data
@Service
public class RestTemplateService {

    private final String INGREDIENT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";
    private final String RECIPE_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
    
    private final RestTemplate restTemplate;

    @Autowired
    public RestTemplateService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<RecipeDto> getRecipes() throws Exception {
        return restTemplate.getForObject(RECIPE_URI, RecipeDtoWrapper.class).getRecipes();
    }

    public List<IngredientDto> getIngredients() throws Exception {
        return restTemplate.getForObject(INGREDIENT_URI, IngredientDtoWrapper.class).getIngredients();
    }

}

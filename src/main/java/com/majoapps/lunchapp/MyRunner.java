package com.majoapps.lunchapp;

import com.majoapps.lunchapp.business.domain.IngredientDto;
import com.majoapps.lunchapp.business.domain.RecipeDto;
import com.majoapps.lunchapp.business.service.IngredientClientService;
import com.majoapps.lunchapp.business.service.RecipeClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class MyRunner implements CommandLineRunner {

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
    }
    
    @Autowired
    private IngredientClientService ingredientClientService;

    @Autowired
    private RecipeClientService recipeClientService;

    @Override
    public void run(String... args) throws Exception {
        
        List<IngredientDto> ingredients = ingredientClientService.getIngredients();
        for (IngredientDto ingredient : ingredients) {		
            System.out.println(ingredient);
        }

        List<RecipeDto> recipes = recipeClientService.getRecipes();
			for (RecipeDto recipe : recipes) {
				System.out.println(recipe);
			}
    }
}
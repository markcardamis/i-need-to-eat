package com.majoapps.lunchapp;

import java.util.List;

import com.majoapps.lunchapp.business.domain.Ingredient;
import com.majoapps.lunchapp.business.domain.IngredientWrapper;
import com.majoapps.lunchapp.business.domain.Recipe;
import com.majoapps.lunchapp.business.domain.RecipeWrapper;
import com.majoapps.lunchapp.business.service.IngredientClientService;
import com.majoapps.lunchapp.business.service.RecipeClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);

    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
    }
    
    @Autowired
    private IngredientClientService ingredients;

    @Autowired
    private RecipeClientService recipes;

    @Override
    public void run(String... args) throws Exception {
        
        IngredientWrapper ingredientList = ingredients.get();
        List<Ingredient> ingredient = ingredientList.getIngredients();
        for (Ingredient ingredientItem : ingredient) {		
            System.out.println(ingredientItem);
        }

        RecipeWrapper recipeList = recipes.get();
        List<Recipe> recipe = recipeList.getRecipes();
			for (Recipe recipeItem : recipe) {
				System.out.println(recipeItem);
			}
    }
}
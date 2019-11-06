package com.majoapps.lunchapp;

import com.majoapps.lunchapp.business.domain.Ingredient;
import com.majoapps.lunchapp.business.domain.IngredientWrapper;
import com.majoapps.lunchapp.business.domain.Recipe;
import com.majoapps.lunchapp.business.domain.RecipeWrapper;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class LunchappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LunchappApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			IngredientWrapper ingredientList = restTemplate.getForObject("http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b", IngredientWrapper.class);
			RecipeWrapper recipeList = restTemplate.getForObject("http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c", RecipeWrapper.class);
			List<Ingredient> ingredient = ingredientList.getIngredients();
			List<Recipe> recipe = recipeList.getRecipes();
			for (Ingredient ingredientItem : ingredient) {		
				System.out.println(ingredientItem);
			}
			for (Recipe recipeItem : recipe) {
				System.out.println(recipeItem);
			}
		};
	}
}

package com.majoapps.lunchapp.data.repository;

import com.majoapps.lunchapp.data.entity.Recipe;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository <Recipe, Integer> {
    List<Recipe> findByTitle(String titleString);
    List<Recipe> findByTitleAndIngredient(String titleString, String ingredientString);
    List<Recipe> findByIngredient(String ingredientString);
}

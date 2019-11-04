package com.majoapps.lunchapp.data.repository;

import com.majoapps.lunchapp.data.entity.Recipe;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository <Recipe, Integer> {
}

package com.majoapps.lunchapp.data.repository;

import java.time.LocalDate;
import java.util.List;
import com.majoapps.lunchapp.data.entity.Ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository <Ingredient, Integer> {
    List<Ingredient> findByTitle(String titleString);
    List<Ingredient> findByUseByAfter(LocalDate useBy);
    List<Ingredient> findAllByOrderByBestBeforeAsc();

}

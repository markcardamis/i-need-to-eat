package com.majoapps.lunchapp.data.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Recipe.class)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @Column
    private String title;

    @Column
    private LocalDate bestBefore;

    @Column
    private LocalDate useBy;

}

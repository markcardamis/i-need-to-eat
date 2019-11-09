package com.majoapps.lunchapp.data.repository;

import java.util.List;

import com.majoapps.lunchapp.data.entity.Lunch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LunchRepository extends JpaRepository <Lunch, Integer> {
    List<Lunch> findAllByOrderByBestBeforeDesc();
    List<Lunch> findAllByOrderByBestBeforeAsc();

}

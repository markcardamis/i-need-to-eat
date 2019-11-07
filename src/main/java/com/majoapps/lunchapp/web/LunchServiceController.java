package com.majoapps.lunchapp.web;

import com.majoapps.lunchapp.business.service.LunchService;
import com.majoapps.lunchapp.data.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/lunch")
public class LunchServiceController {

    // @Autowired
    // private LunchService lunchService;

    // @RequestMapping(method= RequestMethod.GET)
    // public Iterable<Recipe> get() {
    //     return lunchService.getRecipes();
    // }


}

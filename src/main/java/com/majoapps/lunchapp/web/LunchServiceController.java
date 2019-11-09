package com.majoapps.lunchapp.web;

import com.majoapps.lunchapp.business.domain.LunchResponse;
import com.majoapps.lunchapp.business.service.LunchService;
import com.majoapps.lunchapp.data.entity.Lunch;
import com.majoapps.lunchapp.data.entity.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/lunch")
public class LunchServiceController {

    @Autowired
    private LunchService lunchService;

    @RequestMapping(method= RequestMethod.GET)
    public ResponseEntity<LunchResponse> get() {
        LunchResponse lunchResponse = lunchService.get();
        if (lunchResponse == null || lunchResponse.getRecipes().isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(lunchResponse);
    }


}

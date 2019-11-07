package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.IngredientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
@Service
public class IngredientClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5dbf46a5330000f47aa0e55b";

    @Autowired
    RestTemplate restTemplate;
    
    public IngredientWrapper get() throws Exception {
            return restTemplate.getForObject(ROOT_URI, IngredientWrapper.class);
    }

}

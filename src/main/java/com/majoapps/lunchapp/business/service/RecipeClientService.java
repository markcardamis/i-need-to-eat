package com.majoapps.lunchapp.business.service;

import com.majoapps.lunchapp.business.domain.RecipeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.Data;

@Data
@Service
public class RecipeClientService {

    final String ROOT_URI = "http://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";

    @Autowired
    RestTemplate restTemplate;

    public RecipeWrapper get() throws Exception {
        return restTemplate.getForObject(ROOT_URI, RecipeWrapper.class);
    }

}

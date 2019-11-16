package com.majoapps.lunchapp.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.majoapps.lunchapp.business.domain.LunchResponse;
import com.majoapps.lunchapp.business.service.LunchService;
import com.majoapps.lunchapp.data.entity.Lunch;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(LunchServiceController.class)
public class LunchServiceControllerTest {

    @MockBean
    LunchService lunchService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void itShouldReturnOk() throws Exception {
        String recipeTitle = "HotDog";

        Lunch lunch = new Lunch();
        lunch.setId(1);
        lunch.setBestBefore(LocalDate.now().plusDays(1));
        lunch.setTitle(recipeTitle);

        List<Lunch> lunchList = new ArrayList<>(); 
        lunchList.add(lunch);

        LunchResponse lunchResponse = new LunchResponse();
        lunchResponse.setRecipes(lunchList);

        given(lunchService.get()).willReturn(lunchResponse);

        this.mockMvc.perform(get("/api/v1/lunch")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString(recipeTitle)));
    }

}

package com.project.featurestoggle.controllers;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
@AutoConfigureJsonTesters
public class BasicControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    protected void testRequestWithValidationFieldError(
            String url,
            String field,
            String errorMessage,
            String httpRequest,
            String bodyInJson
    ) throws Exception {
        this.mockMvc.perform(
                        (httpRequest.toUpperCase() == "POST" ? post(url) : put(url))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(bodyInJson)
                ).andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.[0].field").value(field))
                .andExpect(jsonPath("$.[0].message").value(errorMessage));
    }
}

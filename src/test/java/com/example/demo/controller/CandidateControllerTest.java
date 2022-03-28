package com.example.demo.controller;


import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

    private static final String PATH = "/candidate";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService service;

    @Test
    void findAll() throws Exception {
        Candidate candidate = new Candidate();
        Mockito.when(this.service.findAll()).thenReturn(Arrays.asList(candidate));

        this.mockMvc.perform(get(PATH)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }
}

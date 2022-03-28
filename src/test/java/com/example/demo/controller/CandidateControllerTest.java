package com.example.demo.controller;


import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CandidateController.class)
public class CandidateControllerTest {

    private static final String PATH = "/candidate";
    private static final String NAME = "New Candidate";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService service;

    @Test
    void findAll() throws Exception {
        Candidate candidate = new Candidate();
        Mockito.when(this.service.findAll()).thenReturn(Arrays.asList(candidate, candidate));

        this.mockMvc.perform(get(PATH)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    void saveCandidate() throws Exception {
        Candidate candidate = new Candidate(NAME);
        Mockito.when(this.service.save(candidate)).thenReturn(candidate);

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonContent = objectWriter.writeValueAsString(new CandidateDto(candidate));

        this.mockMvc.perform(
                    post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    void testValidationNameErrors() throws Exception {
        // Nome vazio a principio
        CandidateDto dto = new CandidateDto();

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonContent = objectWriter.writeValueAsString(dto);

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());

        // Mínimo de três caracteres
        dto.setName("11");
        jsonContent = objectWriter.writeValueAsString(dto);

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());

        // Gerando string com valor maior do que o permitido para o nome
        String bigName = RandomStringUtils.randomAlphanumeric(101);
        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());

    }
}

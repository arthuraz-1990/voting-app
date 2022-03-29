package com.example.demo.controller;

import com.example.demo.VotingAppApplication;
import com.example.demo.config.ModelMapperConfig;
import com.example.demo.dto.ElectionDto;
import com.example.demo.model.Election;
import com.example.demo.service.ElectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ElectionController.class)
public class ElectionControllerTest {

    private static final String PATH = "/election";
    private static final String NAME = "New Election";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ElectionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Teste de listagem")
    void test_FindAll() throws Exception {
        Election election = new Election();
        Mockito.when(this.service.findAll()).thenReturn(Arrays.asList(election, election));

        this.mockMvc.perform(get(PATH)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Teste para persistir candidato")
    void test_SaveElection() throws Exception {
        Election election = new Election(NAME);
        Mockito.when(this.service.save(election)).thenReturn(election);

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = this.objectMapper.writer().withDefaultPrettyPrinter();
        ElectionDto dto = new ModelMapper().map(election, ElectionDto.class);
        String jsonContent = objectWriter.writeValueAsString(dto);

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    @DisplayName("Teste para erros de validação no DTO enviado relativos ao nome")
    void test_Save_ValidationNameErrors() throws Exception {
        // Nome vazio a principio
        ElectionDto dto = new ElectionDto();

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = this.objectMapper.writer().withDefaultPrettyPrinter();
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
        dto.setName(bigName);
        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Teste para deletar a partir de um id")
    void test_Delete() throws Exception {
        long id = 100001L;

        this.mockMvc.perform(delete(PATH.concat("/" + id))).andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste quando um id para deletar não é encontrado")
    void test_Delete_NotFound() throws Exception {
        long id = 100001L;

        Mockito.doThrow(new IllegalArgumentException()).when(this.service).delete(id);

        this.mockMvc.perform(delete(PATH.concat("/" + id))).andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Teste com data com formato válido")
    void test_WithValidDate() throws Exception {
        String dateValue = "\"2022-03-29 00:00:00\"";
        String jsonContent = "{ \"id\": 1, \"name\": \"Teste\", \"startDate\": " + dateValue + ", \"endDate\": null }";

        Mockito.when(this.service.save(ArgumentMatchers.any())).thenReturn(new Election());

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste com data com formato inválido")
    void test_setInvalidDateError() throws Exception {
        String invalidDateValue = "\"abc\"";
        String jsonContent = "{ \"id\": 1, \"name\": \"Teste\", \"startDate\": " + invalidDateValue + ", \"endDate\": null }";

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());
        // Testando com a data fim também inválida
        jsonContent = jsonContent.replace("null", invalidDateValue);

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Teste de erro com a data inicio antes da data fim")
    void test_StartDateBeforeEndDateError() throws Exception {
        String jsonContent = "{ \"id\": 1, \"name\": \"Teste\", \"startDate\": \"2022-03-28 00:00:00\", \"endDate\": \"2021-03-28 00:00:00\" }";

        Mockito.when(this.service.save(ArgumentMatchers.any())).thenReturn(new Election());

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());
    }
}

package com.example.demo.controller;


import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.model.Election;
import com.example.demo.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Teste de listagem")
    void test_FindAll() throws Exception {
        Candidate candidate = new Candidate();
        Mockito.when(this.service.findAll()).thenReturn(Arrays.asList(candidate, candidate));

        this.mockMvc.perform(get(PATH)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Teste de busca por Id")
    void test_FindById() throws Exception {
        UUID uuid = UUID.randomUUID();
        Candidate candidate = new Candidate();

        candidate.setId(uuid);
        Mockito.when(this.service.findById(uuid)).thenReturn(candidate);

        this.mockMvc.perform(get(PATH.concat("/").concat(uuid.toString()))).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").value(uuid.toString()));
    }

    @Test
    @DisplayName("Teste para persistir candidato")
    void test_SaveCandidate() throws Exception {
        Candidate candidate = new Candidate(NAME);
        Mockito.when(this.service.save(candidate)).thenReturn(candidate);

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String jsonContent = objectWriter.writeValueAsString(new ModelMapper().map(candidate, CandidateDto.class));

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
        dto.setName(bigName);
        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("Teste para deletar a partir de um id")
    void test_Delete() throws Exception {
        UUID id = UUID.randomUUID();

        this.mockMvc.perform(delete(PATH.concat("/" + id))).andDo(print()).
                andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)).
                andExpect(status().isOk());
    }

    @Test
    @DisplayName("Teste quando um id para deletar não é encontrado")
    void test_Delete_NotFound() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.doThrow(new IllegalArgumentException()).when(this.service).delete(id);

        this.mockMvc.perform(delete(PATH.concat("/" + id))).andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Erro no formato da URL da Imagem")
    void test_ErrorImageUrlFormat() throws Exception {
        CandidateDto dto = new CandidateDto();
        dto.setImgUrl("abc");
        dto.setName(NAME);

        // Transformando o objeto para uma string json
        ObjectWriter objectWriter = this.objectMapper.writer().withDefaultPrettyPrinter();
        String jsonContent = objectWriter.writeValueAsString(dto);

        this.mockMvc.perform(
                        post(PATH).contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).andDo(print()).
                andExpect(status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));


    }
}

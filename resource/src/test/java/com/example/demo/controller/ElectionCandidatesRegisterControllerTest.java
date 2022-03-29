package com.example.demo.controller;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.service.CandidateElectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ElectionCandidatesRegisterController.class)
public class ElectionCandidatesRegisterControllerTest {

    @MockBean
    private CandidateElectionService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PATH = "/candidates-register";
    private static final String ELECTION_PATH = PATH.concat("/election");


    @Test
    @DisplayName("Buscar lista pelo identificador da Eleição")
    void test_FindListByElectionId() throws Exception {
        long electionId = 2002L;

        ElectionCandidatesRegister register = new ElectionCandidatesRegister(electionId);

        Mockito.when(this.service.findByElectionId(electionId)).thenReturn(register);

        this.mockMvc.perform(get(ELECTION_PATH + "/" + electionId)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$.electionId").value(electionId));

    }

    @Test
    @DisplayName("Erro quando eleição não é encontrada")
    void test_Error_NotFound() throws Exception {
        long electionId = 2002L;
        Mockito.when(this.service.findByElectionId(electionId)).thenReturn(null);
        this.mockMvc.perform(get(ELECTION_PATH + "/" + electionId)).andDo(print()).
            andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Erro quando parâmetro da eleição não é enviado")
    void test_ErrorPathNoParameters() throws Exception {
        this.mockMvc.perform(get(ELECTION_PATH)).andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Adicionar novo candidato a uma eleição")
    void test_addNewCandidate() throws Exception {
        long electionId = 2002L;
        String candidateName = "New Candidate";

        Candidate candidate = new Candidate("New Candidate");
        String jsonContent = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(candidate);

        ElectionCandidatesRegister register = new ElectionCandidatesRegister(electionId);
        List<Candidate> candidateList = List.of(candidate);
        register.setCandidateList(candidateList);

        Mockito.when(this.service.save(electionId, candidate)).thenReturn(register);

        this.mockMvc.perform(
                    post(ELECTION_PATH + "/" + electionId).
                            contentType(MediaType.APPLICATION_JSON).content(jsonContent)
                ).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$.candidateList").isArray()).
                andExpect(jsonPath("$.candidateList[0].name").value(candidateName));
    }


}

package com.example.demo.controller;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.service.ElectionCandidatesRegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

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
    private ElectionCandidatesRegisterService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PATH = "/candidates-register";
    private static final String ELECTION_PATH = PATH.concat("/election");

    @Test
    @DisplayName("Buscar lista pelo identificador da Eleição")
    void test_FindListByElectionId() throws Exception {
        UUID electionId = UUID.randomUUID();

        ElectionCandidatesRegister register = new ElectionCandidatesRegister(electionId);

        Mockito.when(this.service.findByElectionId(electionId)).thenReturn(register);

        this.mockMvc.perform(get(ELECTION_PATH + "/" + electionId)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("$").isNotEmpty()).
                andExpect(jsonPath("$.electionId").value(electionId.toString()));

    }

    @Test
    @DisplayName("Erro quando eleição não é encontrada")
    void test_Error_NotFound() throws Exception {
        UUID electionId = UUID.randomUUID();
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
        UUID electionId = UUID.randomUUID();
        String candidateName = "New Candidate";

        Candidate candidate = new Candidate(candidateName);
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

    private

    @Test
    @DisplayName("Erro ao Adicionar novo candidato - Sem candidato")
    void test_ErrorNoCandidate() throws Exception {
        UUID electionId = UUID.randomUUID();

        this.mockMvc.perform(post(ELECTION_PATH + "/" + electionId)).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Erro ao Adicionar novo candidato - candidato que já existe")
    void test_ErrorCandidateExists() throws Exception {
        UUID electionId = UUID.randomUUID();

        String candidateName = "Old Candidate";

        Candidate candidate = new Candidate(candidateName);
        String jsonContent = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(candidate);

        Mockito.doThrow(new IllegalArgumentException()).when(this.service).save(electionId, candidate);

        this.mockMvc.perform(post(ELECTION_PATH + "/" + electionId).
                        contentType(MediaType.APPLICATION_JSON).content(jsonContent)).
                andDo(print()).
                andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Remover candidato registrado em uma eleição")
    void test_RemoveCandidateExists() throws Exception {
        UUID electionId = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();

        this.mockMvc.perform(delete(ELECTION_PATH + "/" + electionId + "/candidate/" + candidateId)).
                andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)).
                andExpect(content().string("SUCCESS"));
    }

    @Test
    @DisplayName("Erro Remover candidato NÃO registrado em uma eleição ou eleição não existente")
    void test_ErrorRemoveCandidateNotExists() throws Exception {
        UUID electionId = UUID.randomUUID();
        UUID candidateId = UUID.randomUUID();

        Mockito.doThrow(new IllegalArgumentException()).when(this.service).delete(electionId, candidateId);

        this.mockMvc.perform(delete(ELECTION_PATH + "/" + electionId + "/candidate/" + candidateId)).
                andDo(print()).
                andExpect(status().isNotFound());
    }


}

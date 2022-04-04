package com.example.demo.controller;

import com.example.demo.dto.ElectionResultDto;
import com.example.demo.service.ElectionResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ElectionResultController.class)
public class ElectionResultControllerTest {

    private static final String PATH = "/result/election/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ElectionResultService service;

    private static final long ELECTION_ID = 2001L;

    private ElectionResultDto electionResult;

    @BeforeEach
    void setup() {
        this.electionResult = new ElectionResultDto();
        this.electionResult.setElectionId(ELECTION_ID);
        this.electionResult.setCandidatePartialList(List.of());

        Mockito.when(this.service.findById(Mockito.anyLong())).thenAnswer(
                invocationOnMock -> {
                    Long electionId = invocationOnMock.getArgument(0);
                    return electionId.equals(ELECTION_ID) ? this.electionResult : null;
                }
        );
    }

    @Test
    @DisplayName("Busca dos Resultados da Eleição por Id")
    void test_findElectionResultById() throws Exception {

        this.mockMvc.perform(get(PATH + "/" + ELECTION_ID)).andDo(print()).
                andExpect(status().isOk()).
                andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).
                andExpect(MockMvcResultMatchers.jsonPath("$.electionId").value(ELECTION_ID)).
                andExpect(MockMvcResultMatchers.jsonPath("$.candidatePartialList").isArray());
    }

    @Test
    @DisplayName("Erro Eleição Id não encontrada")
    void test_ErrorNotFound() throws Exception {
        this.mockMvc.perform(get(PATH + "/" + (ELECTION_ID + 1))).andDo(print()).
                andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Erro Eleição Id não enviado")
    void test_ErrorWithoutElectionId() throws Exception {
        this.mockMvc.perform(get(PATH)).andDo(print()).
                andExpect(status().isNotFound());
    }

}

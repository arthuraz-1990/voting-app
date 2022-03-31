package com.example.demo.controller;

import com.example.demo.entity.Vote;
import com.example.demo.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(VoteController.class)
public class VoteControllerTest {

    private static final String PATH = "/vote";
    private static final String FIND_BY_ELECTION_PATH = PATH.concat("/election");

    @MockBean
    private VoteService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final long ELECTION_ID_DEFAULT = 2001L;
    private static final long CANDIDATE_ID_DEFAULT = 1001L;
    private static final long USER_ID_DEFAULT = 3001L;

    @Test
    @DisplayName("Buscar todos os votos registrados por eleição")
    void test_findAllVotesByElectionId() throws Exception {
        List<Vote> voteList = List.of(this.createVote());
        Mockito.when(this.service.findByElectionId(ELECTION_ID_DEFAULT)).thenReturn(voteList);

        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_BY_ELECTION_PATH + "/" + ELECTION_ID_DEFAULT)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).
                andExpect(MockMvcResultMatchers.jsonPath("$").isArray()).
                andExpect(MockMvcResultMatchers.jsonPath("$[0].electionId").value(ELECTION_ID_DEFAULT));

    }

    @Test
    @DisplayName("Erro consulta sem parâmetro de eleição")
    void test_ErrorNoElectionParameter() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(FIND_BY_ELECTION_PATH)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Envio de Voto")
    void test_SendVote() throws Exception {
        Vote vote = this.createVote();

        String jsonContent = this.objectMapper.writeValueAsString(vote);

        Mockito.when(this.service.save(vote)).thenReturn(vote);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty()).
                andExpect(MockMvcResultMatchers.jsonPath("$.candidateId").value(vote.getCandidateId())).
                andExpect(MockMvcResultMatchers.jsonPath("$.electionId").value(vote.getElectionId())).
                andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(vote.getUserId()));
    }

    @Test
    @DisplayName("Erro Envio de Voto Parâmetros obrigatórios")
    void test_ErrorSendVoteNotNullParameters() throws Exception {
        Vote vote = this.createVote();
        vote.setCandidateId(null);

        String jsonContent = this.objectMapper.writeValueAsString(vote);

        Mockito.when(this.service.save(vote)).thenReturn(vote);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        vote.setCandidateId(CANDIDATE_ID_DEFAULT);
        vote.setUserId(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        vote.setUserId(USER_ID_DEFAULT);
        vote.setElectionId(null);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    private Vote createVote() {
        Vote vote = new Vote();
        vote.setElectionId(ELECTION_ID_DEFAULT);
        vote.setCandidateId(CANDIDATE_ID_DEFAULT);
        vote.setUserId(USER_ID_DEFAULT);
        vote.setVoteTime(LocalDateTime.now());
        return vote;
    }
}

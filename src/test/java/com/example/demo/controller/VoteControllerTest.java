package com.example.demo.controller;

import com.example.demo.model.Vote;
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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    private static final UUID ELECTION_ID_DEFAULT = UUID.randomUUID();
    private static final UUID CANDIDATE_ID_DEFAULT = UUID.randomUUID();
    private static final String USER_ID_DEFAULT = "tester@test.com";

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
                andExpect(MockMvcResultMatchers.jsonPath("$[0].electionId").value(ELECTION_ID_DEFAULT.toString()));

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
                andExpect(MockMvcResultMatchers.jsonPath("$.candidateId").value(vote.getCandidateId().toString())).
                andExpect(MockMvcResultMatchers.jsonPath("$.electionId").value(vote.getElectionId().toString())).
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

        jsonContent = this.objectMapper.writeValueAsString(vote);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        vote.setUserId(USER_ID_DEFAULT);
        vote.setElectionId(null);

        jsonContent = this.objectMapper.writeValueAsString(vote);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    @DisplayName("Erro com requisição sem o body")
    void test_ErrorNoRequestBody() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof HttpMediaTypeNotSupportedException));

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content("").contentType(MediaType.APPLICATION_JSON)).
                andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException));
    }

    @Test
    @DisplayName("Erro com userId com formato inválido de email")
    void test_InvalidUserIdEmail() throws Exception {
        Vote vote = this.createVote();
        vote.setUserId("abc");

        String jsonContent = this.objectMapper.writeValueAsString(vote);

        Mockito.when(this.service.save(vote)).thenReturn(vote);

        this.mockMvc.perform(MockMvcRequestBuilders.post(PATH).
                        content(jsonContent).contentType(MediaType.APPLICATION_JSON)).
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

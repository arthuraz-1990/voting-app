package com.example.demo.service;

import com.example.demo.model.Vote;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ElectionRepository;
import com.example.demo.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ElectionRepository electionRepository;

    @Mock
    private CandidateRepository candidateRepository;

    private VoteService service;

    private List<Vote> mockList;

    private static final UUID ELECTION_ID_DEFAULT = UUID.randomUUID();
    private static final UUID CANDIDATE_ID_DEFAULT = UUID.randomUUID();
    private static final String USER_ID_DEFAULT = "tester@test.com";

    @BeforeEach
    void setup() {
        this.service = new VoteServiceImpl(voteRepository, electionRepository, candidateRepository);

        this.mockList = new ArrayList<>();
        Vote vote = createVote();

        this.mockList.add(vote);

        Mockito.when(this.voteRepository.findByElectionId(Mockito.any())).thenAnswer(
                invocationOnMock -> {
                    UUID electionId = invocationOnMock.getArgument(0);
                    return this.mockList.stream().filter(v -> v.getElectionId().equals(electionId)).collect(Collectors.toList());
                }
        );

        // Mock dos resultados das verificações se os relacionamentos existem no banco.
        Mockito.when(this.electionRepository.existsById(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0).equals(ELECTION_ID_DEFAULT));
        Mockito.when(this.candidateRepository.existsById(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0).equals(CANDIDATE_ID_DEFAULT));
    }

    private Vote createVote() {
        Vote vote = new Vote();

        vote.setElectionId(ELECTION_ID_DEFAULT);
        vote.setCandidateId(CANDIDATE_ID_DEFAULT);
        vote.setUserId(USER_ID_DEFAULT);
        vote.setVoteTime(LocalDateTime.now());

        return vote;
    }

    @Test
    @DisplayName("Busca de votos por eleição")
    void test_findByElectionId() {


        List<Vote> voteList = this.service.findByElectionId(ELECTION_ID_DEFAULT);

        Assertions.assertNotNull(voteList);
        Assertions.assertFalse(voteList.isEmpty());
        Assertions.assertEquals(voteList.size(), 1);
        Assertions.assertEquals(voteList.get(0).getElectionId(), ELECTION_ID_DEFAULT);
    }

    @Test
    @DisplayName("Salvando novo voto")
    void test_saveVote() {
        Vote vote = this.createVote();

        Mockito.when(this.voteRepository.save(vote)).thenReturn(vote);

        Vote createdVote = this.service.save(vote);

        Assertions.assertNotNull(createdVote);
        Assertions.assertEquals(createdVote.getElectionId(), ELECTION_ID_DEFAULT);
        Assertions.assertEquals(createdVote.getCandidateId(), CANDIDATE_ID_DEFAULT);
        Assertions.assertEquals(createdVote.getUserId(), USER_ID_DEFAULT);
    }

    @Test
    @DisplayName("Erro ao salvar voto, eleição não encontrada")
    void test_errorNotFoundElection() {
        Vote vote = this.createVote();
        // Forçando um id diferente para ele não ser encontrado no mock
        vote.setElectionId(UUID.randomUUID());

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.save(vote));
    }

    @Test
    @DisplayName("Erro ao salvar voto, candidato não encontrada")
    void test_errorNotFoundCandidate() {
        Vote vote = this.createVote();
        // Forçando um id diferente para ele não ser encontrado no mock
        vote.setCandidateId(UUID.randomUUID());

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.save(vote));
    }

    // TODO: Implementar testes para o relacionamento de User.

}

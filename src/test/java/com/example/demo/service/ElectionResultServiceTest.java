package com.example.demo.service;

import com.example.demo.dto.ElectionResultDto;
import com.example.demo.entity.Vote;
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

@SpringBootTest
public class ElectionResultServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ElectionRepository electionRepository;

    private ElectionResultService service;

    private List<Vote> mockVotes;

    private static final long ELECTION_ID = 2001L;

    @BeforeEach
    void setup() {
        this.mockVotes = new ArrayList<>();
        this.service = new ElectionResultServiceImpl(this.voteRepository, this.electionRepository);

        Mockito.when(this.voteRepository.findByElectionId(ELECTION_ID)).thenReturn(this.mockVotes);
        Mockito.when(this.electionRepository.existsById(Mockito.anyLong())).thenAnswer(call -> call.getArgument(0).equals(ELECTION_ID));
    }

    private void mockVotes(long totalVotes, long modParameter) {
        for (int i = 0; i < totalVotes; i++) {
            Vote vote = new Vote();
            vote.setElectionId(ELECTION_ID);
            long candidateId = i % modParameter == 0 ? i : 1;
            vote.setCandidateId(candidateId);
            vote.setVoteTime(LocalDateTime.now());
            vote.setUserId(UUID.randomUUID().toString());
            this.mockVotes.add(vote);
        }
    }

    @Test
    @DisplayName("Eleição sem resultados")
    void test_ElectionNoResults() {
        // Eleição sem votos registrados
        this.mockVotes(0, 0);

        ElectionResultDto electionResultDto = this.service.findById(ELECTION_ID);

        Assertions.assertNotNull(electionResultDto);
        Assertions.assertEquals(electionResultDto.getElectionId(), ELECTION_ID);
        Assertions.assertEquals(electionResultDto.getCandidatePartialList().size(), 0);
        Assertions.assertEquals(electionResultDto.getTotalVotes(), 0);

    }

    @Test
    @DisplayName("Erro Eleição não existe")
    void test_ElectionNotFound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.findById(ELECTION_ID + 1));
    }
}

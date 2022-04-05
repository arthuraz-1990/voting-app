package com.example.demo.service;

import com.example.demo.dto.CandidateResultDto;
import com.example.demo.dto.ElectionResultDto;
import com.example.demo.model.Vote;
import com.example.demo.repository.ElectionRepository;
import com.example.demo.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class ElectionResultServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ElectionRepository electionRepository;

    private ElectionResultService service;

    private List<Vote> mockVotes;

    private static final UUID ELECTION_ID = UUID.randomUUID();

    private static final UUID CANDIDATE_ID = UUID.randomUUID();

    @BeforeEach
    void setup() {
        this.mockVotes = new ArrayList<>();
        this.service = new ElectionResultServiceImpl(this.voteRepository, this.electionRepository);

        Mockito.when(this.voteRepository.findByElectionId(ELECTION_ID)).thenReturn(this.mockVotes);
        Mockito.when(this.electionRepository.existsById(Mockito.any())).thenAnswer(call -> call.getArgument(0).equals(ELECTION_ID));
    }

    private void mockVotes(long totalVotes, long modParameter) {
        for (int i = 1; i <= totalVotes; i++) {
            Vote vote = new Vote();
            vote.setElectionId(ELECTION_ID);
            UUID candidateId = i % modParameter > 0 ? UUID.randomUUID() : CANDIDATE_ID;
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
        Assertions.assertEquals(electionResultDto.getCandidateResultList().size(), 0);
        Assertions.assertEquals(electionResultDto.getTotalVotes(), 0);

    }

    @Test
    @DisplayName("Erro Eleição não existe")
    void test_ElectionNotFound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Eleição por unanimidade")
    void test_ElectionUnanimity() {
        long totalVotes = 10;
        // Eleição com todos os votos para apenas um candidato
        this.mockVotes(totalVotes, 1);

        ElectionResultDto electionResultDto = this.service.findById(ELECTION_ID);
        Assertions.assertNotNull(electionResultDto);
        Assertions.assertEquals(electionResultDto.getElectionId(), ELECTION_ID);
        Assertions.assertEquals(electionResultDto.getCandidateResultList().size(), 1);
        Assertions.assertEquals(electionResultDto.getTotalVotes(), totalVotes);

        CandidateResultDto candidateResultDto = electionResultDto.getCandidateResultList().get(0);

        Assertions.assertNotNull(candidateResultDto);
        Assertions.assertEquals(candidateResultDto.getTotalVotes(), totalVotes);
        Assertions.assertEquals(candidateResultDto.getVotePercentage(), 100.0);
        Assertions.assertEquals(candidateResultDto.getCandidateId(), CANDIDATE_ID);

        double sumPercentage = electionResultDto.getCandidateResultList().stream().mapToDouble(CandidateResultDto::getVotePercentage).sum();
        Assertions.assertEquals(sumPercentage, 100.0);
    }

    @Test
    @DisplayName("Eleição com um candidato com 20%")
    void test_ElectionCandidateWith50Percent() {
        long totalVotes = 10;
        // Eleição com todos os votos para apenas um candidato
        this.mockVotes(totalVotes, 2);

        ElectionResultDto electionResultDto = this.service.findById(ELECTION_ID);
        Assertions.assertNotNull(electionResultDto);
        Assertions.assertEquals(electionResultDto.getElectionId(), ELECTION_ID);
        Assertions.assertEquals(electionResultDto.getCandidateResultList().size(), 6);
        Assertions.assertEquals(electionResultDto.getTotalVotes(), totalVotes);

        Optional<CandidateResultDto> candidateResultDtoOpt = electionResultDto.getCandidateResultList().
                stream().filter(r -> r.getCandidateId() == CANDIDATE_ID).findFirst();

        Assertions.assertTrue(candidateResultDtoOpt.isPresent());
        CandidateResultDto candidateResultDto = candidateResultDtoOpt.get();
        Assertions.assertEquals(candidateResultDto.getTotalVotes(), 5);
        Assertions.assertEquals(candidateResultDto.getVotePercentage(), 50.0);
        Assertions.assertEquals(candidateResultDto.getCandidateId(), CANDIDATE_ID);

        double sumPercentage = electionResultDto.getCandidateResultList().stream().mapToDouble(CandidateResultDto::getVotePercentage).sum();
        Assertions.assertEquals(sumPercentage, 100.0);
    }

    @Test
    @DisplayName("Eleição com um candidato com 2 votos em 10")
    void test_ElectionCandidateWith20Percent() {
        long totalVotes = 10;
        // Eleição com todos os votos para apenas um candidato
        this.mockVotes(totalVotes, 4);

        ElectionResultDto electionResultDto = this.service.findById(ELECTION_ID);
        Assertions.assertNotNull(electionResultDto);
        Assertions.assertEquals(electionResultDto.getElectionId(), ELECTION_ID);
        Assertions.assertEquals(electionResultDto.getCandidateResultList().size(), 9);
        Assertions.assertEquals(electionResultDto.getTotalVotes(), totalVotes);

        Optional<CandidateResultDto> candidateResultDtoOpt = electionResultDto.getCandidateResultList().
                stream().filter(r -> r.getCandidateId() == CANDIDATE_ID).findFirst();

        Assertions.assertTrue(candidateResultDtoOpt.isPresent());
        CandidateResultDto candidateResultDto = candidateResultDtoOpt.get();
        Assertions.assertEquals(candidateResultDto.getTotalVotes(), 2);
        Assertions.assertEquals(candidateResultDto.getVotePercentage(), 20.0);
        Assertions.assertEquals(candidateResultDto.getCandidateId(), CANDIDATE_ID);

        double sumPercentage = electionResultDto.getCandidateResultList().stream().mapToDouble(CandidateResultDto::getVotePercentage).sum();
        Assertions.assertEquals(sumPercentage, 100.0);
    }

}

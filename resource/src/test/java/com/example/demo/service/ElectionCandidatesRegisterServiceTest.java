package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ElectionCandidatesRegisterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ElectionCandidatesRegisterServiceTest {

    @Mock
    private ElectionCandidatesRegisterRepository registerRepository;

    @Mock
    private CandidateRepository candidateRepository;

    private ElectionCandidatesRegisterService service;

    private ElectionCandidatesRegister mockRegister;

    private static final long ELECTION_ID_DEFAULT = 2001L;

    @BeforeEach
    void setup() {
        this.service = new ElectionCandidatesRegisterServiceImpl(registerRepository, candidateRepository);
        String candidateName = "Candidato Atual";
        Candidate candidate = new Candidate(candidateName);

        ElectionCandidatesRegister register = new ElectionCandidatesRegister(ELECTION_ID_DEFAULT);
        register.setCandidateList(List.of(candidate));

        this.mockRegister = register;
    }

    @Test
    @DisplayName("Teste para busca de registros pelo identificador da Eleição")
    void test_FindByElectionId() {

        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        ElectionCandidatesRegister register = this.service.findByElectionId(ELECTION_ID_DEFAULT);

        Assertions.assertNotNull(register);
        Assertions.assertFalse(register.getCandidateList().isEmpty());
        Assertions.assertEquals(register.getElectionId(), ELECTION_ID_DEFAULT);
        Assertions.assertTrue(register.getCandidateList().get(0).getName().contains("Atual"));

    }

    @Test
    @DisplayName("Teste para busca de registros pelo identificador da Eleição")
    void test_NotFoundFindByElectionId() {

        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT + 1)).thenReturn(Optional.empty());
        ElectionCandidatesRegister register = this.service.findByElectionId(ELECTION_ID_DEFAULT + 1);

        Assertions.assertNull(register);

    }


}

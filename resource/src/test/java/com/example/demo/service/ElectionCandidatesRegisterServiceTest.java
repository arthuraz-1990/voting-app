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

import java.util.ArrayList;
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
    private static final long CANDIDATE_ID_DEFAULT = 1001L;

    @BeforeEach
    void setup() {
        this.service = new ElectionCandidatesRegisterServiceImpl(registerRepository, candidateRepository);
        String candidateName = "Existing Candidate";
        Candidate candidate = new Candidate(candidateName);
        candidate.setId(CANDIDATE_ID_DEFAULT);

        ElectionCandidatesRegister register = new ElectionCandidatesRegister(ELECTION_ID_DEFAULT);
        List<Candidate> candidateList = new ArrayList<>();
        candidateList.add(candidate);
        register.setCandidateList(candidateList);

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
    
    @Test
    @DisplayName("Teste para adicionar candidato")
    void test_AddNewCandidateToElection() {
        Candidate newCandidate = createNewCandidate();

        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        Mockito.when(this.registerRepository.save(this.mockRegister)).thenReturn(this.mockRegister);

        ElectionCandidatesRegister register = this.service.save(ELECTION_ID_DEFAULT, newCandidate);
        Assertions.assertNotNull(register);
        Assertions.assertFalse(register.getCandidateList().isEmpty());
        Assertions.assertEquals(register.getCandidateList().size(), 2);
        Assertions.assertTrue(register.getCandidateList().stream().anyMatch(c -> c.getName().equals(newCandidate.getName())));
    }

    @Test
    @DisplayName("Erro ao adicionar candidato que já existe")
    void test_Error_AddExistingCandidateToElection() {
        Candidate newCandidate = createNewCandidate();
        // Deixando com o mesmo id que já existe
        newCandidate.setId(CANDIDATE_ID_DEFAULT);

        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        Mockito.when(this.registerRepository.save(this.mockRegister)).thenReturn(this.mockRegister);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.save(ELECTION_ID_DEFAULT, newCandidate));
    }

    @Test
    @DisplayName("Erro ao adicionar à eleição com id não encontrado")
    void test_Error_ElectionNotFoundAddNewCandidate() {
        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT + 1)).thenReturn(Optional.empty());
        Mockito.when(this.registerRepository.save(this.mockRegister)).thenReturn(this.mockRegister);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.save(ELECTION_ID_DEFAULT, createNewCandidate()));
    }

    @Test
    @DisplayName("Erro ao adicionar sem candidato")
    void test_Error_AddNoCandidate() {
        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.save(ELECTION_ID_DEFAULT, null));
    }

    @Test
    @DisplayName("Remover Candidato de uma eleição")
    void test_RemoveCandidate() {
        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        Mockito.when(this.registerRepository.save(this.mockRegister)).thenReturn(this.mockRegister);
        this.service.delete(ELECTION_ID_DEFAULT, CANDIDATE_ID_DEFAULT);

        Assertions.assertFalse(mockRegister.getCandidateList().stream().anyMatch(c -> c.getId().equals(CANDIDATE_ID_DEFAULT)));
        Assertions.assertTrue(mockRegister.getCandidateList().isEmpty());
    }
    @Test
    @DisplayName("Erro ao remover candidato não registrado na eleição")
    void test_ErrorRemoveCandidateNotRegistered() {
        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT)).thenReturn(Optional.of(this.mockRegister));
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.delete(ELECTION_ID_DEFAULT, CANDIDATE_ID_DEFAULT + 1));
    }

    @Test
    @DisplayName("Erro ao remover candidato de uma eleição não encontrada")
    void test_ErrorRemoveCandidateElectionNotFound() {
        Mockito.when(this.registerRepository.findById(ELECTION_ID_DEFAULT + 1)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.service.delete(ELECTION_ID_DEFAULT + 1, CANDIDATE_ID_DEFAULT));
    }

    private Candidate createNewCandidate() {
        Candidate newCandidate = new Candidate();
        newCandidate.setId(CANDIDATE_ID_DEFAULT + 1);
        newCandidate.setName("New Candidate");
        return newCandidate;
    }


}

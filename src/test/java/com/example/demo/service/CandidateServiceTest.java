package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class CandidateServiceTest {

    @Mock
    private CandidateRepository repository;

    @Test
    @DisplayName("Teste de listagem de candidatos")
    void test_FindAll() {
        Candidate c = new Candidate();
        Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(c, c));
        List<Candidate> candidateList = this.createService().findAll();

        assertThat(candidateList.isEmpty()).isFalse();
        assertEquals(candidateList.size(), 2);
    }

    @Test
    @DisplayName("Teste para Persistência de um candidato")
    void test_Save() {
        Candidate c = new Candidate();
        Mockito.when(this.repository.save(c)).thenReturn(c);

        c = this.createService().save(c);
        assertNotEquals(c, null);
    }

    @Test
    @DisplayName("Teste para deletar um candidato por id")
    void test_Delete() {
        long id = 10001L;
        Mockito.when(this.repository.existsById(id)).thenReturn(Boolean.TRUE);

        this.createService().delete(id);
    }

    @Test
    @DisplayName("Teste de Erro ao deletar candidato quando não for encontrado")
    void test_Delete_notFound() {
        long id = 10001L;
        Mockito.when(this.repository.existsById(id)).thenReturn(Boolean.FALSE);

        assertThrows(IllegalArgumentException.class, () -> this.createService().delete(id));
    }

    private CandidateService createService() {
        return new CandidateServiceImpl(this.repository);
    }
}

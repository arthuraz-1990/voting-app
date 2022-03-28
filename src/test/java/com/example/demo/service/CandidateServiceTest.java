package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


@SpringBootTest
public class CandidateServiceTest {

    @Mock
    private CandidateRepository repository;

    @Test
    void findAll() {
        Candidate c = new Candidate();
        Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(c, c));
        List<Candidate> candidateList = this.createService().findAll();

        assertThat(!candidateList.isEmpty());
        assertEquals(candidateList.size(), 2);
    }

    @Test
    void save() {
        Candidate c = new Candidate();
        Mockito.when(this.repository.save(c)).thenReturn(c);

        c = this.createService().save(c);
        assertNotEquals(c, null);
    }

    void delete() {
        long id = 1;
        
    }

    private CandidateService createService() {
        return new CandidateServiceImpl(this.repository);
    }
}

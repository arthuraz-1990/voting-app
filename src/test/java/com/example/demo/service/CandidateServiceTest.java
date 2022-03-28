package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class CandidateServiceTest {

    @Mock
    private CandidateRepository repository;

    @InjectMocks
    private CandidateService service = new CandidateServiceImpl(repository);

    @Test
    void findAll() {
        Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(new Candidate()));
        List<Candidate> candidateList = this.service.findAll();

        assertThat(!candidateList.isEmpty());
    }
}

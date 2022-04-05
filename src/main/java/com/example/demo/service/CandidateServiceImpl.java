package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.repository.CandidateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository repository;

    public CandidateServiceImpl(CandidateRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Candidate> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Candidate save(Candidate candidate) {
        return this.repository.save(candidate);
    }

    @Override
    public void delete(UUID id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Entity not found");
        }
    }
}

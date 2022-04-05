package com.example.demo.service;

import com.example.demo.model.Candidate;

import java.util.List;
import java.util.UUID;

public interface CandidateService {

    List<Candidate> findAll();

    Candidate save(Candidate candidate);

    void delete(UUID id);
}

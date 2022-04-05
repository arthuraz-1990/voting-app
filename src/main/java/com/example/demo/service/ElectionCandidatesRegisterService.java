package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;

import java.util.UUID;

public interface ElectionCandidatesRegisterService {

    ElectionCandidatesRegister findByElectionId(UUID electionId);

    ElectionCandidatesRegister save(UUID electionId, Candidate candidate);

    void delete(UUID electionId, UUID candidateId);
}

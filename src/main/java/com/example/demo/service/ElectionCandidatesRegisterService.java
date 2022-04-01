package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;

public interface ElectionCandidatesRegisterService {

    ElectionCandidatesRegister findByElectionId(long electionId);

    ElectionCandidatesRegister save(long electionId, Candidate candidate);

    void delete(long electionId, long candidateId);
}

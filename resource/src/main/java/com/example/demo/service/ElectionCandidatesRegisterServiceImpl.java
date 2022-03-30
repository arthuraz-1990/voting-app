package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ElectionCandidatesRegisterRepository;
import org.springframework.stereotype.Service;

@Service
public class ElectionCandidatesRegisterServiceImpl implements ElectionCandidatesRegisterService {

    private final ElectionCandidatesRegisterRepository registerRepository;

    public ElectionCandidatesRegisterServiceImpl(
            ElectionCandidatesRegisterRepository registerRepository,
            CandidateRepository candidateRepository
    ) {
        this.registerRepository = registerRepository;
    }

    @Override
    public ElectionCandidatesRegister findByElectionId(long electionId) {
        return this.registerRepository.findById(electionId).orElse(null);
    }

    @Override
    public ElectionCandidatesRegister save(long electionId, Candidate candidate) {
        return null;
    }

    @Override
    public void delete(long electionId, long candidateId) {

    }
}

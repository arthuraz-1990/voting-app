package com.example.demo.service;

import com.example.demo.model.Vote;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ElectionRepository;
import com.example.demo.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public VoteServiceImpl(
            VoteRepository voteRepository,
            ElectionRepository electionRepository,
            CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public List<Vote> findByElectionId(long electionId) {
        return this.voteRepository.findByElectionId(electionId);
    }

    @Override
    public Vote save(Vote vote) {
        if (!this.electionRepository.existsById(vote.getElectionId())) {
            throw new IllegalArgumentException("Eleição não encontrada.");
        }
        if (!this.candidateRepository.existsById(vote.getCandidateId())) {
            throw new IllegalArgumentException("Candidato não encontrado.");
        }

        return this.voteRepository.save(vote);
    }
}

package com.example.demo.service;

import com.example.demo.dto.ElectionResultDto;
import com.example.demo.repository.ElectionRepository;
import com.example.demo.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class ElectionResultServiceImpl implements ElectionResultService {

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;

    public ElectionResultServiceImpl(VoteRepository voteRepository, ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
    }

    @Override
    public ElectionResultDto findById(long electionId) {
        if (!this.electionRepository.existsById(electionId)) {
            throw new IllegalArgumentException("Eleição não encontrada.");
        }

        ElectionResultDto electionResultDto = new ElectionResultDto();
        electionResultDto.setElectionId(electionId);
        return electionResultDto;
    }
}

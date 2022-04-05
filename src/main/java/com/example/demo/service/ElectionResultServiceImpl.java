package com.example.demo.service;

import com.example.demo.dto.CandidateResultDto;
import com.example.demo.dto.ElectionResultDto;
import com.example.demo.model.Vote;
import com.example.demo.repository.ElectionRepository;
import com.example.demo.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Vote> voteList = this.voteRepository.findByElectionId(electionId);

        if (!voteList.isEmpty()) {
            // Lista dos Ids de candidatos...
            List<Long> candidatesIds = voteList.stream().map(Vote::getCandidateId).distinct().collect(Collectors.toList());

            // Transformando os candidatos no seu resumo de resultado
            List<CandidateResultDto> candidateResultList = candidatesIds.stream().map(candidateId ->
                buildCandidateResult(voteList, candidateId)
            ).collect(Collectors.toList());

            electionResultDto.setCandidateResultList(candidateResultList);
        }

        return electionResultDto;
    }

    private CandidateResultDto buildCandidateResult(List<Vote> voteList, Long candidateId) {
        int totalVotes = voteList.size();

        CandidateResultDto candidateResultDto = new CandidateResultDto();
        candidateResultDto.setCandidateId(candidateId);

        // Contando o total de votos para o candidato
        long candidateVotes = voteList.stream().filter(v -> v.getCandidateId().equals(candidateId)).count();
        candidateResultDto.setTotalVotes(candidateVotes);

        // Porcentagem do candidato frente ao total
        double candidatePercentage = this.calculatePercentage(candidateVotes, totalVotes);
        candidateResultDto.setVotePercentage(candidatePercentage);

        return candidateResultDto;
    }

    private double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }

}

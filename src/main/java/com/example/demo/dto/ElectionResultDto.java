package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class ElectionResultDto {

    @Schema(description = "Identificador da Eleição")
    private long electionId;

    @Schema(description = "Listagem com os resultados por candidato")
    private List<CandidateResultDto> candidateResultList;

    public ElectionResultDto() {
        this.candidateResultList = new ArrayList<>();
    }

    public long getElectionId() {
        return electionId;
    }

    public void setElectionId(long electionId) {
        this.electionId = electionId;
    }

    public List<CandidateResultDto> getCandidateResultList() {
        return candidateResultList;
    }

    public void setCandidateResultList(List<CandidateResultDto> candidateResultList) {
        this.candidateResultList = candidateResultList;
    }

    @Schema(description = "Total de Votos na Eleição")
    public long getTotalVotes() {
        return this.getCandidateResultList().isEmpty() ? 0 :
                this.getCandidateResultList().stream().mapToLong(CandidateResultDto::getTotalVotes).sum();
    }
}

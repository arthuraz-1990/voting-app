package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CandidateResultDto {

    @Schema(description = "Identificador do Candidato")
    private long candidateId;
    @Schema(description = "Total de Votos do Candidato")
    private long totalVotes;
    @Schema(description = "Porcentagem de Votos do Candidato")
    private double votePercentage;


    public long getTotalVotes() {
        return this.totalVotes;
    }

    public void setTotalVotes(long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public double getVotePercentage() {
        return this.votePercentage;
    }

    public void setVotePercentage(double votePercentage) {
        this.votePercentage = votePercentage;
    }

    public long getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(long candidateId) {
        this.candidateId = candidateId;
    }
}

package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class CandidateResultDto {

    @Schema(description = "Identificador do Candidato")
    private UUID candidateId;
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

    public UUID getCandidateId() {
        return this.candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }
}

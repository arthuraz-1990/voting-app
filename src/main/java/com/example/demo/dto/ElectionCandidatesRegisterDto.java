package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ElectionCandidatesRegisterDto {

    @Schema(description = "Identificador da Eleição")
    private UUID electionId;

    @Schema(description = "Listagem de Candidatos registrados")
    private List<CandidateDto> candidateList;

    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public List<CandidateDto> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<CandidateDto> candidateList) {
        this.candidateList = candidateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionCandidatesRegisterDto that = (ElectionCandidatesRegisterDto) o;
        return Objects.equals(electionId, that.electionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionId);
    }

    @Override
    public String toString() {
        return "ElectionCandidatesRegisterDto{" +
                "electionId=" + electionId +
                '}';
    }
}

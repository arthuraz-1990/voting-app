package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Objects;

public class ElectionCandidatesRegisterDto {

    @ApiModelProperty("Identificador da Eleição")
    private Long electionId;

    @ApiModelProperty("Listagem de Candidatos registrados")
    private List<CandidateDto> candidateList;

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
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

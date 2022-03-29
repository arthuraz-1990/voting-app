package com.example.demo.model;

import java.util.List;
import java.util.Objects;

public class ElectionCandidatesRegister {

    private Long electionId;

    private List<Candidate> candidateList;

    public ElectionCandidatesRegister() {
    }

    public ElectionCandidatesRegister(Long electionId) {
        this.electionId = electionId;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public List<Candidate> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<Candidate> candidateList) {
        this.candidateList = candidateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionCandidatesRegister register = (ElectionCandidatesRegister) o;
        return Objects.equals(electionId, register.electionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionId);
    }

    @Override
    public String toString() {
        return "ElectionCandidatesRegister{" +
                "electionId=" + electionId +
                '}';
    }
}

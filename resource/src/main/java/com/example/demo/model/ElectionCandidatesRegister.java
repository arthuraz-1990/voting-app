package com.example.demo.model;

import java.util.List;

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
}

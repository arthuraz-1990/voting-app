package com.example.demo.dto;

import java.util.List;

public class ElectionResultDto {

    private long electionId;

    private List candidatePartialList;


    public long getElectionId() {
        return electionId;
    }

    public void setElectionId(long electionId) {
        this.electionId = electionId;
    }

    public List getCandidatePartialList() {
        return candidatePartialList;
    }

    public void setCandidatePartialList(List candidatePartialList) {
        this.candidatePartialList = candidatePartialList;
    }
}

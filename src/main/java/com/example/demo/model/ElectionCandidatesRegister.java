package com.example.demo.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class ElectionCandidatesRegister implements Serializable {

    @Id
    private UUID electionId;

    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Candidate> candidateList;

    public ElectionCandidatesRegister() {
    }

    public ElectionCandidatesRegister(UUID electionId) {
        this.electionId = electionId;
    }

    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
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

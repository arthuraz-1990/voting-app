package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private UUID electionId;
    @Column(nullable = false)
    private UUID candidateId;
    @Column(nullable = false)
    private String userId;
    @Version
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voteTime;

    @ManyToOne
    @JoinColumn(name = "electionId", insertable = false, updatable = false)
    private Election election;

    @ManyToOne
    @JoinColumn(name = "candidateId", insertable = false, updatable = false)
    private Candidate candidate;

    // TODO: 31/03/2022 Adicionar relacionamento com tabela de usuários

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getElectionId() {
        return electionId;
    }

    public void setElectionId(UUID electionId) {
        this.electionId = electionId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(LocalDateTime voteTime) {
        this.voteTime = voteTime;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public Election getElection() {
        return election;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "electionId=" + electionId +
                ", candidateId=" + candidateId +
                ", userId=" + userId +
                ", voteTime=" + voteTime +
                '}';
    }
}

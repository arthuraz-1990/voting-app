package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long electionId;
    @Column(nullable = false)
    private Long candidateId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
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

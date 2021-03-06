package com.example.demo.service;

import com.example.demo.model.Vote;

import java.util.List;
import java.util.UUID;

public interface VoteService {
    List<Vote> findByElectionId(UUID electionId);

    Vote save(Vote vote);
}

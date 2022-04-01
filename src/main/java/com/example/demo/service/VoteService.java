package com.example.demo.service;

import com.example.demo.entity.Vote;

import java.util.List;

public interface VoteService {
    List<Vote> findByElectionId(long electionIdDefault);

    Vote save(Vote vote);
}

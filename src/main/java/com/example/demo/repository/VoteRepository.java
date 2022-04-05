package com.example.demo.repository;

import com.example.demo.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    List<Vote> findByElectionId(Long electionId);

}

package com.example.demo.controller;

import com.example.demo.dto.VoteDTO;
import com.example.demo.entity.Vote;
import com.example.demo.service.VoteService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "vote")
public class VoteController {

    private final VoteService service;
    private final ModelMapper modelMapper;

    public VoteController(VoteService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping(value = "election/{electionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VoteDTO> findByElectionId(@PathVariable Long electionId) {
        return this.service.findByElectionId(electionId).stream().map(v -> this.modelMapper.map(v, VoteDTO.class)).
                collect(Collectors.toList());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public VoteDTO sendVote(@Valid @RequestBody VoteDTO voteDTO) {
        Vote vote = this.modelMapper.map(voteDTO, Vote.class);
        vote = this.service.save(vote);
        return this.modelMapper.map(vote, VoteDTO.class);
    }
}

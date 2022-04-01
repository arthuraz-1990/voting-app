package com.example.demo.controller;

import com.example.demo.dto.VoteDTO;
import com.example.demo.entity.Vote;
import com.example.demo.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(description = "Controller com os serviços de Votos", name = "Controller de Votos")
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
    @Operation(summary = "Busca de votos por Identificador da Eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de votos por eleição")
    })
    public List<VoteDTO> findByElectionId(
            @PathVariable @Parameter(description ="Identificador da Eleição") Long electionId) {
        return this.service.findByElectionId(electionId).stream().map(v -> this.modelMapper.map(v, VoteDTO.class)).
                collect(Collectors.toList());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Enviar um voto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado"),
            @ApiResponse(responseCode = "400", description = "Erro ao registrar voto")
    })
    public VoteDTO sendVote(
            @Valid @RequestBody @Parameter(description = "Descrição do Voto") VoteDTO voteDTO) {
        Vote vote = this.modelMapper.map(voteDTO, Vote.class);
        vote = this.service.save(vote);
        return this.modelMapper.map(vote, VoteDTO.class);
    }
}

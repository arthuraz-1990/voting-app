package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.ElectionCandidatesRegisterDto;
import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.service.ElectionCandidatesRegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Tag(description = "Controller com os serviços de registros de Candidatos em Eleições", name = "Controller de Registro de Candidatos")
@RestController
@RequestMapping(value = "candidates-register")
public class ElectionCandidatesRegisterController {

    private final ElectionCandidatesRegisterService service;
    private final ModelMapper mapper;

    public ElectionCandidatesRegisterController(ElectionCandidatesRegisterService service) {
        this.service = service;
        this.mapper = new ModelMapper();
    }

    @GetMapping(value = "election/{electionId}")
    @Operation(summary = "Listar registros de Candidatos por eleição")
    ElectionCandidatesRegisterDto findByElectionId(
            @PathVariable @Parameter(description = "Identificador da Eleição") Long electionId) {
        ElectionCandidatesRegister register = this.service.findByElectionId(electionId);
        if (register != null) {
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Eleição não encontrada");
    }

    @PostMapping(value = "election/{electionId}")
    @Operation(summary = "Registrar um Candidato para uma eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato registrado"),
            @ApiResponse(responseCode = "400", description = "Erro ao registrar candidato")
    })
    ElectionCandidatesRegisterDto registerCandidate(
            @PathVariable @Parameter(description = "Identificador da Eleição") Long electionId,
            @Valid @RequestBody @Parameter(description = "Descrição do Candidato") CandidateDto candidateDto) {
        Candidate candidate = new ModelMapper().map(candidateDto, Candidate.class);
        try {
            ElectionCandidatesRegister register = this.service.save(electionId, candidate);
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "election/{electionId}/candidate/{candidateId}")
    @Operation(summary = "Remover o registro de um candidato de uma eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato removido"),
            @ApiResponse(responseCode = "404", description = "Candidato não registrado ou não encontrado"),
    })
    ResponseEntity<String> removeCandidateFromElection(
            @PathVariable @Parameter(description = "Identificador da Eleição") Long electionId,
            @PathVariable @Parameter(description = "Identificador do Candidato") Long candidateId) {
        try {
            this.service.delete(electionId, candidateId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("SUCCESS");
    }



}

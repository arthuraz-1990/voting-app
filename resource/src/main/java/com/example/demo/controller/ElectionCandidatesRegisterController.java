package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.ElectionCandidatesRegisterDto;
import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.service.ElectionCandidatesRegisterService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Api("Controller de Registro de Candidatos nas Eleições")
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
    @ApiOperation("Listar registros de Candidatos por eleição")
    ElectionCandidatesRegisterDto findByElectionId(
            @PathVariable @ApiParam("Identificador da Eleição") Long electionId) {
        ElectionCandidatesRegister register = this.service.findByElectionId(electionId);
        if (register != null) {
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Eleição não encontrada");
    }

    @PostMapping(value = "election/{electionId}")
    @ApiOperation("Registrar um Candidato para uma eleição")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Candidato registrado"),
            @ApiResponse(code = 400, message = "Erro ao registrar candidato")
    })
    ElectionCandidatesRegisterDto registerCandidate(
            @PathVariable @ApiParam("Identificador da Eleição") Long electionId,
            @Valid @RequestBody @ApiParam("Descrição do Candidato") CandidateDto candidateDto) {
        Candidate candidate = new ModelMapper().map(candidateDto, Candidate.class);
        try {
            ElectionCandidatesRegister register = this.service.save(electionId, candidate);
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping(value = "election/{electionId}/candidate/{candidateId}")
    @ApiOperation("Remover o registro de um candidato de uma eleição")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Candidato removido"),
            @ApiResponse(code = 404, message = "Candidato não registrado ou não encontrado"),
    })
    ResponseEntity<String> removeCandidateFromElection(
            @PathVariable @ApiParam("Identificador da Eleição") Long electionId,
            @PathVariable @ApiParam("Identificador do Candidato") Long candidateId) {
        try {
            this.service.delete(electionId, candidateId);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.ok("SUCCESS");
    }



}

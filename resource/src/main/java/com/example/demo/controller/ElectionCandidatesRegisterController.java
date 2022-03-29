package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.dto.ElectionCandidatesRegisterDto;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.service.CandidateElectionService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "candidates-register")
public class ElectionCandidatesRegisterController {

    private final CandidateElectionService service;
    private final ModelMapper mapper;

    public ElectionCandidatesRegisterController(CandidateElectionService service) {
        this.service = service;
        this.mapper = new ModelMapper();
    }

    @GetMapping(value = "election/{electionId}")
    @ApiOperation("Listar registros de Candidatos por eleição")
    ElectionCandidatesRegisterDto findByElectionId(@PathVariable Long electionId) {
        ElectionCandidatesRegister register = this.service.findByElectionId(electionId);
        if (register != null) {
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Eleição não encontrada");
    }

    @PostMapping(value = "election/{electionId}")
    @ApiOperation("Registrar um Candidato para uma eleição")
    ElectionCandidatesRegisterDto registerCandidate(@PathVariable Long electionId, @RequestBody CandidateDto candidateDto) {
        ElectionCandidatesRegister register = this.service.findByElectionId(electionId);
        if (register != null) {
            return this.mapper.map(register, ElectionCandidatesRegisterDto.class);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Eleição não encontrada");
    }



}

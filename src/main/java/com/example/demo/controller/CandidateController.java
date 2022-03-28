package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/candidate")
public class CandidateController {

    private final CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "Retorna lista de candidatos")
    @ResponseBody
    List<CandidateDto> findAll() {
        return this.service.findAll().stream().map(CandidateDto::new).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Salva um novo candidato")
    @ResponseBody
    CandidateDto save(@Valid @RequestBody CandidateDto candidate) {
        Candidate c = new Candidate(candidate.getName());
        c = this.service.save(c);
        return new CandidateDto(c);
    }
}

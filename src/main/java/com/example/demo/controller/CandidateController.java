package com.example.demo.controller;

import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    List<Candidate> findAll() {
        return this.service.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Salva um novo candidato")
    @ResponseBody
    Candidate save(@RequestBody Candidate candidate) {
        return this.service.save(candidate);
    }
}

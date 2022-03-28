package com.example.demo.controller;

import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private CandidateService service;

    public CandidateController(CandidateService service) {
        this.service = service;
    }

    @GetMapping
    List<Candidate> findAll() {
        return this.service.findAll();
    }
}

package com.example.demo.dto;

import com.example.demo.model.Candidate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CandidateDto {

    private long id;

    @NotEmpty
    @Size(min = 3, max = 100, message = "Tamanho do Nome deve estar entre 3 e 100 caracteres")
    private String name;

    public CandidateDto() {
    }

    public CandidateDto(Candidate candidate) {
        this.id = candidate.getId();
        this.name = candidate.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

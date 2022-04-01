package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CandidateDto {

    @Schema(description = "Identificador do Candidato")
    private Long id;

    @Schema(description = "Nome do Candidato")
    @NotEmpty
    @Size(min = 3, max = 100, message = "Tamanho do Nome deve estar entre 3 e 100 caracteres")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

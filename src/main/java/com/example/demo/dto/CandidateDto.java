package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

public class CandidateDto {

    @Schema(description = "Identificador do Candidato")
    private UUID id;

    @Schema(description = "Nome do Candidato")
    @NotEmpty
    @Size(min = 3, max = 100, message = "Tamanho do Nome deve estar entre 3 e 100 caracteres")
    private String name;

    @Schema(description = "Imagem do Candidato")
    @URL(regexp = "^*.(jpg|jpeg|png)")
    private String imgUrl;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

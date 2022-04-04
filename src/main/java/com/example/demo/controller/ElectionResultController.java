package com.example.demo.controller;

import com.example.demo.dto.ElectionResultDto;
import com.example.demo.service.ElectionResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

@Tag(description = "Controller com os serviços de Resultados", name = "Controller de Resultados")
@RestController
@RequestMapping("/result/election")
public class ElectionResultController {

    private final ElectionResultService service;

    public ElectionResultController(ElectionResultService service) {
        this.service = service;
    }

    @GetMapping(value = "{electionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca de Resultados por Identificador da Eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de votos por eleição")
    })
    public ElectionResultDto getResultById(
        @PathVariable @Parameter(description ="Identificador da Eleição") Long electionId) {
        try {
            return this.service.findById(electionId);
        } catch (IllegalArgumentException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
}

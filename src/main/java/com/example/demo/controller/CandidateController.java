package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(description = "Controller com os serviços de Candidatos", name = "Controller de Candidatos")
@RestController
@RequestMapping(value = "/candidate")
public class CandidateController {

    private final CandidateService service;
    private final ModelMapper modelMapper;

    public CandidateController(CandidateService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @Operation(summary = "Retorna lista de candidatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna a lista de candidatos"),
            @ApiResponse(responseCode = "400", description = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @ResponseBody
    List<CandidateDto> findAll() {
        return this.service.findAll().stream().map(c -> this.modelMapper.map(c, CandidateDto.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Salva um novo candidato")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Novo candidato salvo"),
            @ApiResponse(responseCode = "400", description = "Erro ao salvar candidato")
    })
    CandidateDto save(
            @Valid @RequestBody @Parameter(description = "Descrição do Candidato") CandidateDto candidate) {
        Candidate c = this.modelMapper.map(candidate, Candidate.class);
        c = this.service.save(c);
        return this.modelMapper.map(c, CandidateDto.class);
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Remove um candidato pelo seu Identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato removido"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
    })
    ResponseEntity<String> delete(
            @PathVariable @Parameter(description = "Identificador do Candidato") Long id) {
        try {
            this.service.delete(id);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.example.demo.controller;

import com.example.demo.dto.CandidateDto;
import com.example.demo.model.Candidate;
import com.example.demo.service.CandidateService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api("Controller de Candidatos")
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
    @ApiOperation(value = "Retorna lista de candidatos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a lista de candidatos"),
            @ApiResponse(code = 400, message = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
    })
    @ResponseBody
    List<CandidateDto> findAll() {
        return this.service.findAll().stream().map(c -> this.modelMapper.map(c, CandidateDto.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Salva um novo candidato")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Novo candidato salvo"),
            @ApiResponse(code = 400, message = "Erro ao salvar candidato")
    })
    CandidateDto save(
            @Valid @RequestBody @ApiParam("Descrição do Candidato") CandidateDto candidate) {
        Candidate c = this.modelMapper.map(candidate, Candidate.class);
        c = this.service.save(c);
        return this.modelMapper.map(c, CandidateDto.class);
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Remove um candidato pelo seu Identificador")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Candidato removido"),
            @ApiResponse(code = 404, message = "Candidato não encontrado"),
    })
    ResponseEntity<String> delete(
            @PathVariable @ApiParam("Identificador do Candidato") Long id) {
        try {
            this.service.delete(id);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

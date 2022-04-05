package com.example.demo.controller;

import com.example.demo.dto.ElectionDto;
import com.example.demo.model.Election;
import com.example.demo.service.ElectionService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(description = "Controller com os serviços de Eleições", name = "Controller de Eleições")
@RestController
@RequestMapping(value = "/election")
public class ElectionController {

    private final ElectionService service;
    private final ModelMapper modelMapper;

    public ElectionController(ElectionService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retorna lista de eleições")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de Eleições")
    })
    public List<ElectionDto> findAll() {
        return this.service.findAll().stream().map(e -> this.modelMapper.map(e, ElectionDto.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Salva uma nova Eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleição salva"),
            @ApiResponse(responseCode = "400", description = "Erro ao salvar eleição")
    })
    ElectionDto save(
            @Valid @RequestBody @Parameter(description ="Descrição da Eleição") ElectionDto electionDto) {
        Election c = this.modelMapper.map(electionDto, Election.class);
        c = this.service.save(c);
        return this.modelMapper.map(c, ElectionDto.class);
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "Remove uma Eleição pelo seu Identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleição removida"),
            @ApiResponse(responseCode = "404", description = "Eleição não encontrada"),
    })
    ResponseEntity<String> delete(
            @PathVariable @Parameter(description ="Identificador da Eleição") UUID id) {
        try {
            this.service.delete(id);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

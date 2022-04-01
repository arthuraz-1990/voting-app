package com.example.demo.controller;

import com.example.demo.dto.ElectionDto;
import com.example.demo.model.Election;
import com.example.demo.service.ElectionService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api("Controller de Eleições")
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
    @ApiOperation(value = "Retorna lista de eleições")
    public List<ElectionDto> findAll() {
        return this.service.findAll().stream().map(e -> this.modelMapper.map(e, ElectionDto.class)).collect(Collectors.toList());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Salva uma nova Eleição")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Eleição salva"),
            @ApiResponse(code = 400, message = "Erro ao salvar eleição")
    })
    ElectionDto save(
            @Valid @RequestBody @ApiParam("Descrição da Eleição") ElectionDto electionDto) {
        Election c = this.modelMapper.map(electionDto, Election.class);
        c = this.service.save(c);
        return this.modelMapper.map(c, ElectionDto.class);
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Remove uma Eleição pelo seu Identificador")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Eleição removida"),
            @ApiResponse(code = 404, message = "Eleição não encontrada"),
    })
    ResponseEntity<String> delete(
            @PathVariable @ApiParam("Identificador da Eleição") Long id) {
        try {
            this.service.delete(id);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

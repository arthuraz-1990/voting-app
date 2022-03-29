package com.example.demo.controller;

import com.example.demo.dto.ElectionDto;
import com.example.demo.model.Election;
import com.example.demo.service.ElectionService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    ElectionDto save(@Valid @RequestBody ElectionDto electionDto) {
        Election c = this.modelMapper.map(electionDto, Election.class);
        c = this.service.save(c);
        return this.modelMapper.map(c, ElectionDto.class);
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Remove uma Eleição pelo seu Identificador")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            this.service.delete(id);
            return ResponseEntity.ok("SUCCESS");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.example.demo.service;

import com.example.demo.model.Election;

import java.util.List;
import java.util.UUID;

public interface ElectionService {

    List<Election> findAll();

    Election save(Election election);

    void delete(UUID id);

    Election findById(UUID randomUUID);
}

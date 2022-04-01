package com.example.demo.service;

import com.example.demo.model.Election;

import java.util.List;

public interface ElectionService {

    List<Election> findAll();

    Election save(Election election);

    void delete(long id);
}

package com.example.demo.service;

import com.example.demo.model.Election;
import com.example.demo.repository.ElectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository repository;

    public ElectionServiceImpl(ElectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Election> findAll() {
        return this.repository.findAll();
    }

    @Override
    public Election save(Election election) {
        return this.repository.save(election);
    }

    @Override
    public void delete(UUID id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Objeto não encontrado");
        }
    }
}

package com.example.demo.service;

import com.example.demo.dto.ElectionResultDto;

import java.util.UUID;

public interface ElectionResultService {
    ElectionResultDto findById(UUID electionId);
}

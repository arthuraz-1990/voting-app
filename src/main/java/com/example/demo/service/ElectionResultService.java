package com.example.demo.service;

import com.example.demo.dto.ElectionResultDto;

public interface ElectionResultService {
    ElectionResultDto findById(long electionId);
}

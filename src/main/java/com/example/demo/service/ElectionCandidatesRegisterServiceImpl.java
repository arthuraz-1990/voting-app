package com.example.demo.service;

import com.example.demo.model.Candidate;
import com.example.demo.model.ElectionCandidatesRegister;
import com.example.demo.repository.CandidateRepository;
import com.example.demo.repository.ElectionCandidatesRegisterRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ElectionCandidatesRegisterServiceImpl implements ElectionCandidatesRegisterService {

    private final ElectionCandidatesRegisterRepository registerRepository;

    public ElectionCandidatesRegisterServiceImpl(
            ElectionCandidatesRegisterRepository registerRepository,
            CandidateRepository candidateRepository
    ) {
        this.registerRepository = registerRepository;
    }

    @Override
    public ElectionCandidatesRegister findByElectionId(long electionId) {
        return this.registerRepository.findById(electionId).orElse(null);
    }

    @Override
    public ElectionCandidatesRegister save(long electionId, Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidato não enviado.");
        }

        ElectionCandidatesRegister election = this.registerRepository.findById(electionId).
                orElseThrow(() -> new IllegalArgumentException("Eleição não encontrada"));

        // Verificando se um candidato com o mesmo id foi adicionado anteriormente
        if (!election.getCandidateList().isEmpty() && election.getCandidateList().contains(candidate)) {
            throw new IllegalArgumentException("Candidato já registrado anteriormente");
        }

        election.getCandidateList().add(candidate);

        return this.registerRepository.save(election);
    }

    @Override
    public void delete(long electionId, long candidateId) {
        ElectionCandidatesRegister election = this.registerRepository.findById(electionId).
                orElseThrow(() -> new IllegalArgumentException("Eleição não encontrada"));
        //
        Optional<Candidate> candidate = election.getCandidateList().stream().filter(c -> c.getId().equals(candidateId)).findAny();
        if (candidate.isEmpty()) {
            throw new IllegalArgumentException("Candidato não está associado à esta eleição");
        }

        election.getCandidateList().remove(candidate.get());
        this.registerRepository.save(election);
    }
}

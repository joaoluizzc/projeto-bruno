package com.faculdade.atividade.service;

import com.faculdade.atividade.model.Professor;
import com.faculdade.atividade.repository.ProfessorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProfessorService {

    private final ProfessorRepository repository;

    public ProfessorService(ProfessorRepository repository) {
        this.repository = repository;
    }

    public List<Professor> listarTodos() {
        return repository.findAll();
    }

    public Professor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor nao encontrado"));
    }

    @Transactional
    public Professor criar(Professor professor) {
        normalizar(professor);
        validarNomeDuplicado(professor.getNome(), null);
        professor.setId(null);
        return repository.save(professor);
    }

    @Transactional
    public Professor atualizar(Long id, Professor dados) {
        Professor professor = buscarPorId(id);
        normalizar(dados);
        validarNomeDuplicado(dados.getNome(), id);

        professor.setNome(dados.getNome());
        professor.setEmail(dados.getEmail());
        professor.setMateria(dados.getMateria());

        return repository.save(professor);
    }

    @Transactional
    public void deletar(Long id) {
        Professor professor = buscarPorId(id);
        repository.delete(professor);
    }

    private void validarNomeDuplicado(String nome, Long idAtual) {
        repository.findByNomeIgnoreCase(nome)
                .filter(professor -> idAtual == null || !professor.getId().equals(idAtual))
                .ifPresent(professor -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Ja existe um professor cadastrado com esse nome"
                    );
                });
    }

    private void normalizar(Professor professor) {
        professor.setNome(professor.getNome().trim());
        professor.setEmail(professor.getEmail().trim());
        professor.setMateria(professor.getMateria().trim());
    }
}

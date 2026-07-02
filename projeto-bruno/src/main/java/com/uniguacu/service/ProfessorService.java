package com.uniguacu.service;

import com.uniguacu.model.Curso;
import com.uniguacu.model.Professor;
import com.uniguacu.repository.CursoRepository;
import com.uniguacu.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository repository;
    private final CursoRepository cursoRepository;

    public List<Professor> listarTodos() {
        return repository.findAll();
    }

    public Professor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor nao encontrado"));
    }

    public List<Curso> listarCursosCoordenados(Long id) {
        buscarPorId(id);
        return cursoRepository.findByCoordenadorId(id);
    }

    @Transactional
    public Professor salvar(Professor professor) {
        return repository.save(professor);
    }

    @Transactional
    public Professor atualizar(Long id, Professor professorDetails) {
        Professor professor = buscarPorId(id);
        professor.setNome(professorDetails.getNome());
        professor.setEmail(professorDetails.getEmail());
        return repository.save(professor);
    }

    @Transactional
    public void deletar(Long id) {
        Professor professor = buscarPorId(id);
        if (cursoRepository.existsByCoordenadorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor possui cursos vinculados");
        }
        repository.delete(professor);
    }
}

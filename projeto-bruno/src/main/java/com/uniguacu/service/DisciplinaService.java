package com.uniguacu.service;

import com.uniguacu.dto.DisciplinaRequest;
import com.uniguacu.model.Curso;
import com.uniguacu.model.Disciplina;
import com.uniguacu.repository.CursoRepository;
import com.uniguacu.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplinaService {

    private final DisciplinaRepository repository;
    private final CursoRepository cursoRepository;

    public List<Disciplina> listarTodos() {
        return repository.findAll();
    }

    public Disciplina buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina nao encontrada"));
    }

    public List<Disciplina> listarPorCurso(Long cursoId) {
        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso nao encontrado");
        }
        return repository.findByCursoId(cursoId);
    }

    @Transactional
    public Disciplina salvar(DisciplinaRequest request) {
        Disciplina disciplina = new Disciplina();
        aplicarDados(disciplina, request);
        return repository.save(disciplina);
    }

    @Transactional
    public Disciplina atualizar(Long id, DisciplinaRequest request) {
        Disciplina disciplina = buscarPorId(id);
        aplicarDados(disciplina, request);
        return repository.save(disciplina);
    }

    @Transactional
    public void deletar(Long id) {
        Disciplina disciplina = buscarPorId(id);
        repository.delete(disciplina);
    }

    private void aplicarDados(Disciplina disciplina, DisciplinaRequest request) {
        Curso curso = cursoRepository.findById(request.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso nao encontrado"));

        disciplina.setNome(request.nome());
        disciplina.setCurso(curso);
    }
}

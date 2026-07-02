package com.uniguacu.service;

import com.uniguacu.model.AreaConhecimento;
import com.uniguacu.model.Curso;
import com.uniguacu.repository.AreaConhecimentoRepository;
import com.uniguacu.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AreaConhecimentoService {

    private final AreaConhecimentoRepository repository;
    private final CursoRepository cursoRepository;

    public List<AreaConhecimento> listarTodos() {
        return repository.findAll();
    }

    public AreaConhecimento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Area do conhecimento nao encontrada"));
    }

    public List<Curso> listarCursos(Long id) {
        buscarPorId(id);
        return cursoRepository.findByAreaConhecimentoId(id);
    }

    @Transactional
    public AreaConhecimento salvar(AreaConhecimento area) {
        return repository.save(area);
    }

    @Transactional
    public AreaConhecimento atualizar(Long id, AreaConhecimento areaDetails) {
        AreaConhecimento area = buscarPorId(id);
        area.setNome(areaDetails.getNome());
        return repository.save(area);
    }

    @Transactional
    public void deletar(Long id) {
        AreaConhecimento area = buscarPorId(id);
        if (cursoRepository.existsByAreaConhecimentoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Area possui cursos vinculados");
        }
        repository.delete(area);
    }
}

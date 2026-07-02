package com.uniguacu.service;

import com.uniguacu.dto.CursoCompletoResponse;
import com.uniguacu.dto.CursoRequest;
import com.uniguacu.model.AreaConhecimento;
import com.uniguacu.model.Curso;
import com.uniguacu.model.Disciplina;
import com.uniguacu.model.Professor;
import com.uniguacu.repository.AreaConhecimentoRepository;
import com.uniguacu.repository.CursoRepository;
import com.uniguacu.repository.DisciplinaRepository;
import com.uniguacu.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final AreaConhecimentoRepository areaConhecimentoRepository;
    private final ProfessorRepository professorRepository;
    private final DisciplinaRepository disciplinaRepository;

    public List<Curso> listarTodos() {
        return repository.findAll();
    }

    public Curso buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso nao encontrado"));
    }

    public List<Disciplina> listarDisciplinas(Long id) {
        buscarPorId(id);
        return disciplinaRepository.findByCursoId(id);
    }

    public CursoCompletoResponse buscarCompleto(Long id) {
        Curso curso = buscarPorId(id);
        List<Disciplina> disciplinas = disciplinaRepository.findByCursoId(id);
        return new CursoCompletoResponse(
                curso.getId(),
                curso.getNome(),
                curso.getAreaConhecimento(),
                curso.getCoordenador(),
                disciplinas
        );
    }

    @Transactional
    public Curso salvar(CursoRequest request) {
        Curso curso = new Curso();
        aplicarDados(curso, request);
        return repository.save(curso);
    }

    @Transactional
    public Curso atualizar(Long id, CursoRequest request) {
        Curso curso = buscarPorId(id);
        aplicarDados(curso, request);
        return repository.save(curso);
    }

    @Transactional
    public void deletar(Long id) {
        Curso curso = buscarPorId(id);
        if (disciplinaRepository.existsByCursoId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Curso possui disciplinas vinculadas");
        }
        repository.delete(curso);
    }

    private void aplicarDados(Curso curso, CursoRequest request) {
        AreaConhecimento areaConhecimento = areaConhecimentoRepository.findById(request.areaConhecimentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Area do conhecimento nao encontrada"));

        Professor coordenador = professorRepository.findById(request.coordenadorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor coordenador nao encontrado"));

        curso.setNome(request.nome());
        curso.setAreaConhecimento(areaConhecimento);
        curso.setCoordenador(coordenador);
    }
}

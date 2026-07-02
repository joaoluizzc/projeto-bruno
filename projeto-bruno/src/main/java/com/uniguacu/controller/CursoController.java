package com.uniguacu.controller;

import com.uniguacu.dto.CursoCompletoResponse;
import com.uniguacu.dto.CursoRequest;
import com.uniguacu.model.Curso;
import com.uniguacu.model.Disciplina;
import com.uniguacu.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/cursos", "/api/cursos"})
@RequiredArgsConstructor
@Tag(name = "Cursos", description = "CRUD de cursos")
public class CursoController {

    private final CursoService service;

    @GetMapping
    @Operation(summary = "Lista todos os cursos")
    public List<Curso> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um curso por ID")
    public Curso buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/disciplinas")
    @Operation(summary = "Lista disciplinas de um curso")
    public List<Disciplina> listarDisciplinas(@PathVariable Long id) {
        return service.listarDisciplinas(id);
    }

    @GetMapping("/{id}/completo")
    @Operation(summary = "Busca a estrutura completa de um curso")
    public CursoCompletoResponse buscarCompleto(@PathVariable Long id) {
        return service.buscarCompleto(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra um curso")
    public ResponseEntity<Curso> criar(@Valid @RequestBody CursoRequest curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(curso));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um curso")
    public Curso atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest curso) {
        return service.atualizar(id, curso);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um curso")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

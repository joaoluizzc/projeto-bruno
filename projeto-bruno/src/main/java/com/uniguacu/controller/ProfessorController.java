package com.uniguacu.controller;

import com.uniguacu.model.Curso;
import com.uniguacu.model.Professor;
import com.uniguacu.service.ProfessorService;
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
@RequestMapping({"/professores", "/api/professores"})
@RequiredArgsConstructor
@Tag(name = "Professores", description = "CRUD de professores")
public class ProfessorController {

    private final ProfessorService service;

    @GetMapping
    @Operation(summary = "Lista todos os professores")
    public List<Professor> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um professor por ID")
    public Professor buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/cursos")
    @Operation(summary = "Lista cursos coordenados por um professor")
    public List<Curso> listarCursosCoordenados(@PathVariable Long id) {
        return service.listarCursosCoordenados(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra um professor")
    public ResponseEntity<Professor> criar(@Valid @RequestBody Professor professor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(professor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um professor")
    public Professor atualizar(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        return service.atualizar(id, professor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um professor")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

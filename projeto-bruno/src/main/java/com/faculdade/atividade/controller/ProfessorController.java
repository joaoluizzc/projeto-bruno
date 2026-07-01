package com.faculdade.atividade.controller;

import com.faculdade.atividade.model.Professor;
import com.faculdade.atividade.service.ProfessorService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @GetMapping
    public List<Professor> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Professor buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Professor> criar(@Valid @RequestBody Professor professor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(professor));
    }

    @PutMapping("/{id}")
    public Professor atualizar(@PathVariable Long id, @Valid @RequestBody Professor professor) {
        return service.atualizar(id, professor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

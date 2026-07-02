package com.uniguacu.controller;

import com.uniguacu.dto.DisciplinaRequest;
import com.uniguacu.model.Disciplina;
import com.uniguacu.service.DisciplinaService;
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
@RequestMapping({"/disciplinas", "/api/disciplinas"})
@RequiredArgsConstructor
@Tag(name = "Disciplinas", description = "CRUD de disciplinas")
public class DisciplinaController {

    private final DisciplinaService service;

    @GetMapping
    @Operation(summary = "Lista todas as disciplinas")
    public List<Disciplina> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma disciplina por ID")
    public Disciplina buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra uma disciplina vinculada a um curso")
    public ResponseEntity<Disciplina> criar(@Valid @RequestBody DisciplinaRequest disciplina) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(disciplina));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma disciplina")
    public Disciplina atualizar(@PathVariable Long id, @Valid @RequestBody DisciplinaRequest disciplina) {
        return service.atualizar(id, disciplina);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma disciplina")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

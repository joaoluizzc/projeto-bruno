package com.uniguacu.controller;

import com.uniguacu.model.AreaConhecimento;
import com.uniguacu.model.Curso;
import com.uniguacu.service.AreaConhecimentoService;
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
@RequestMapping({"/areas", "/api/areas", "/api/areas-conhecimento"})
@RequiredArgsConstructor
@Tag(name = "Areas do Conhecimento", description = "CRUD de areas do conhecimento")
public class AreaConhecimentoController {

    private final AreaConhecimentoService service;

    @GetMapping
    @Operation(summary = "Lista todas as areas do conhecimento")
    public List<AreaConhecimento> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma area do conhecimento por ID")
    public AreaConhecimento buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @GetMapping("/{id}/cursos")
    @Operation(summary = "Lista cursos de uma area do conhecimento")
    public List<Curso> listarCursos(@PathVariable Long id) {
        return service.listarCursos(id);
    }

    @PostMapping
    @Operation(summary = "Cadastra uma area do conhecimento")
    public ResponseEntity<AreaConhecimento> criar(@Valid @RequestBody AreaConhecimento area) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(area));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma area do conhecimento")
    public AreaConhecimento atualizar(@PathVariable Long id, @Valid @RequestBody AreaConhecimento area) {
        return service.atualizar(id, area);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma area do conhecimento")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

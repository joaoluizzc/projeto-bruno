package com.uniguacu.dto;

import com.uniguacu.model.AreaConhecimento;
import com.uniguacu.model.Disciplina;
import com.uniguacu.model.Professor;

import java.util.List;

public record CursoCompletoResponse(
        Long id,
        String nome,
        AreaConhecimento areaConhecimento,
        Professor coordenador,
        List<Disciplina> disciplinas
) {
}

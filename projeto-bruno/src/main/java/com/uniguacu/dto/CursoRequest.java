package com.uniguacu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoRequest(
        @NotBlank(message = "O nome do curso e obrigatorio")
        String nome,

        @NotNull(message = "A area do conhecimento e obrigatoria")
        Long areaConhecimentoId,

        @NotNull(message = "O coordenador e obrigatorio")
        Long coordenadorId
) {
}

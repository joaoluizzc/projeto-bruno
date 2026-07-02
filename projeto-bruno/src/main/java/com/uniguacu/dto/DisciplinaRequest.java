package com.uniguacu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DisciplinaRequest(
        @NotBlank(message = "O nome da disciplina e obrigatorio")
        String nome,

        @NotNull(message = "O curso da disciplina e obrigatorio")
        Long cursoId
) {
}

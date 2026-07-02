package com.uniguacu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do curso e obrigatorio")
    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "area_conhecimento_id", nullable = false)
    @NotNull(message = "A area do conhecimento e obrigatoria")
    private AreaConhecimento areaConhecimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coordenador_id", nullable = false)
    @NotNull(message = "O coordenador e obrigatorio")
    private Professor coordenador;
}

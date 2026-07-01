package com.faculdade.atividade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
        name = "professores",
        uniqueConstraints = @UniqueConstraint(name = "uk_professor_nome", columnNames = "nome")
)
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do professor e obrigatorio")
    @Column(nullable = false, unique = true)
    private String nome;

    @NotBlank(message = "O email do professor e obrigatorio")
    @Email(message = "Informe um email valido")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "A materia e obrigatoria")
    @Column(nullable = false)
    private String materia;

    public Professor() {
    }

    public Professor(Long id, String nome, String email, String materia) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.materia = materia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }
}

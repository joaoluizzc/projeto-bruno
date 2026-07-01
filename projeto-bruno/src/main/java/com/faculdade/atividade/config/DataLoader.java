package com.faculdade.atividade.config;

import com.faculdade.atividade.model.Professor;
import com.faculdade.atividade.repository.ProfessorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProfessorRepository repository;

    public DataLoader(ProfessorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        repository.save(new Professor(null, "Ana Martins", "ana.martins@faculdade.edu", "Programacao Web"));
        repository.save(new Professor(null, "Carlos Silva", "carlos.silva@faculdade.edu", "Banco de Dados"));
        repository.save(new Professor(null, "Marina Costa", "marina.costa@faculdade.edu", "Engenharia de Software"));
    }
}

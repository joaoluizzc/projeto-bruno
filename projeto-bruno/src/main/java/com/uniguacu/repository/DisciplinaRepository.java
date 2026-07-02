package com.uniguacu.repository;

import com.uniguacu.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    List<Disciplina> findByCursoId(Long cursoId);

    boolean existsByCursoId(Long cursoId);
}

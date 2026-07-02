package com.uniguacu.repository;

import com.uniguacu.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByAreaConhecimentoId(Long areaConhecimentoId);

    List<Curso> findByCoordenadorId(Long coordenadorId);

    boolean existsByAreaConhecimentoId(Long areaConhecimentoId);

    boolean existsByCoordenadorId(Long coordenadorId);
}

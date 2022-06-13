package org.cvg.mockito.repository;

import org.cvg.mockito.models.Examen;

import java.util.List;

public interface ExamenRepository {
    List<Examen> findAll();
    Examen guardar(Examen examen);
}

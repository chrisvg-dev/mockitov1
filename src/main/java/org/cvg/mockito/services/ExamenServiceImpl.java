package org.cvg.mockito.services;

import org.cvg.mockito.models.Examen;
import org.cvg.mockito.repository.ExamenRepository;
import org.cvg.mockito.repository.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen>  findExamenPorNombre(String nombre) {
        return this.examenRepository.findAll().stream()
                .filter( examen -> examen.getNombre().contains(nombre) )
                .findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId( examen.getId() );
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardarExamen(Examen examen) {
        if (!examen.getPreguntas().isEmpty()){
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return this.examenRepository.guardar(examen);
    }
}

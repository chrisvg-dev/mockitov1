package org.cvg.mockito.services;

import org.cvg.mockito.models.Examen;

import java.util.Arrays;
import java.util.List;

public class DatosExamen {
    public static final List<Examen> EXAMENES = Arrays.asList(
            new Examen(5L, "Math"),
                new Examen(6L, "Chemistry"),
                new Examen(7L, "History"),
                new Examen(8L, "Physical"),
                new Examen(9L, "Music")
        );
    public static final List<String> PREGUNTAS = Arrays.asList("Aritmetica", "Integrales", "Derivadas", "Geometria", "Trigonometria", "Programacion");

    public static final Examen EXAMEN = new Examen(null, "Redes");
}

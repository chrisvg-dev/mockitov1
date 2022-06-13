package org.cvg.mockito.services;

import org.cvg.mockito.models.Examen;
import org.cvg.mockito.repository.ExamenRepository;
import org.cvg.mockito.repository.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExamenServiceImplTest {

    private ExamenRepository repository;
    private PreguntaRepository preguntaRepository;
    private ExamenService service;


    @BeforeEach
    void setUp() {
        //ExamenRepository repository = new ExamenRepositoryImpl();
        this.repository = mock(ExamenRepository.class);
        this.preguntaRepository = mock( PreguntaRepository.class );
        this.service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    void findExamenPorNombre() {
        when( repository.findAll() ).thenReturn( DatosExamen.EXAMENES ); //Mockito when
        String name = "Math";
        Optional<Examen> examen = service.findExamenPorNombre(name);
        assertAll(
                () -> assertTrue( examen.isPresent() ),
                () -> assertEquals( 5, examen.orElseThrow().getId() ),
                () -> assertEquals( name, examen.orElseThrow().getNombre() )
        );
    }

    @Test
    void findExamenPorNombreEmptyList() {
        List<Examen> examenes = Collections.emptyList();
        when( repository.findAll() ).thenReturn( examenes ); //Mockito when
        String name = "Math";
        Optional<Examen> examen = service.findExamenPorNombre(name);

        assertFalse( examen.isPresent() );
    }

    @Test
    void testPreguntasExamen() {
        when( repository.findAll() ).thenReturn(DatosExamen.EXAMENES );
        //when( preguntaRepository.findPreguntasPorExamenId(7L) ).thenReturn( DatosExamen.PREGUNTAS );
        when( preguntaRepository.findPreguntasPorExamenId( anyLong() ) ).thenReturn( DatosExamen.PREGUNTAS );
        Examen examen = service.findExamenPorNombreConPreguntas("History");

        assertEquals(6, examen.getPreguntas().size());
        assertTrue( examen.getPreguntas().contains( "Aritmetica" ) );
    }


    /**
     * TEST VERIFY MOCKITO. REVISA QUE LAS DEPENDENCIAS EJECUTEN LOS METODOS NECESARIOS
     */
    @Test
    void testPreguntasExamenVerify() {
        when( repository.findAll() ).thenReturn(DatosExamen.EXAMENES );
        //when( preguntaRepository.findPreguntasPorExamenId(7L) ).thenReturn( DatosExamen.PREGUNTAS );
        when( preguntaRepository.findPreguntasPorExamenId( anyLong() ) ).thenReturn( DatosExamen.PREGUNTAS );
        Examen examen = service.findExamenPorNombreConPreguntas("Math");

        //assertEquals(6, examen.getPreguntas().size());
        //assertTrue( examen.getPreguntas().contains( "Aritmetica" ) );

        verify( repository ).findAll(); // Verifica si se invoco el metodo, si no falla
        verify( preguntaRepository ).findPreguntasPorExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when( repository.findAll() ).thenReturn( Collections.emptyList() );
        //when( preguntaRepository.findPreguntasPorExamenId(7L) ).thenReturn( DatosExamen.PREGUNTAS );
        when( preguntaRepository.findPreguntasPorExamenId( anyLong() ) ).thenReturn( DatosExamen.PREGUNTAS );
        Examen examen = service.findExamenPorNombreConPreguntas("Math");

        assertNull(examen);
        verify( repository ).findAll(); // Verifica si se invoco el metodo, si no falla
        verify( preguntaRepository ).findPreguntasPorExamenId(5L);
    }
}
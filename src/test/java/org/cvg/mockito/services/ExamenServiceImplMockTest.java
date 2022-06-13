package org.cvg.mockito.services;

import org.cvg.mockito.models.Examen;
import org.cvg.mockito.repository.ExamenRepository;
import org.cvg.mockito.repository.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Se ejecuta si no se agrega el MockitoAnnotations
class ExamenServiceImplMockTest {
    // MOCK
    @Mock
    private ExamenRepository repository;
    @Mock private PreguntaRepository preguntaRepository;

    // SERVICE
    @InjectMocks private ExamenServiceImpl service;
    //@InjectMocks private ExamenService service;


    @BeforeEach
    void setUp() {
        //  MockitoAnnotations.openMocks(this); Se usa si no se agrega el extendedWith
        //ExamenRepository repository = new ExamenRepositoryImpl();
        //this.repository = mock(ExamenRepository.class);
        //this.preguntaRepository = mock( PreguntaRepository.class );
        //this.service = new ExamenServiceImpl(repository, preguntaRepository);
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

    @Disabled
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

    @Test
    void testGuardarExamen() {
        Examen newExamen = DatosExamen.EXAMEN;
        newExamen.setPreguntas( DatosExamen.PREGUNTAS );
        when( repository.guardar( any(Examen.class)) ).thenReturn( DatosExamen.EXAMEN );
        Examen examen = service.guardarExamen( newExamen  );

        assertNotNull( examen.getId() );
        assertEquals( 8L, examen.getId() );
        assertEquals( "Redes", examen.getNombre() );

        verify( repository ).guardar( any(Examen.class) );
        verify( preguntaRepository ).guardarVarias( anyList() );
    }

    @Test
    void testGuardarExamenConIdIncremental() {
        // Given
        Examen newExamen = DatosExamen.EXAMEN;
        newExamen.setPreguntas( DatosExamen.PREGUNTAS );
        when( repository.guardar( any(Examen.class)) ).then( new Answer<Examen>(){

            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId( secuencia++ );
                return examen;
            }
        });

        // When
        Examen examen = service.guardarExamen( newExamen  );

        // Then

        assertNotNull( examen.getId() );
        assertEquals( 8L, examen.getId() );
        assertEquals( "Redes", examen.getNombre() );

        verify( repository ).guardar( any(Examen.class) );
        verify( preguntaRepository ).guardarVarias( anyList() );
    }
}
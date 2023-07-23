package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.models.Team;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class PilotoCorridaServiceTest extends BaseTest {

    @Autowired
    PilotRaceService pilotoCorridaService;

    @Test
    @DisplayName("Teste buscar resultado por ID")
    @Sql({"classpath:/resources/sqls/piloto_corrida.sql"})
    void findByIdTest() {
        var pilotoCorrida = pilotoCorridaService.findById(5);
        assertNotNull(pilotoCorrida);
        assertEquals(5, pilotoCorrida.getId());
        assertEquals("1", pilotoCorrida.getPlacement());
        assertEquals(5, pilotoCorrida.getPilot());
        assertEquals(5, pilotoCorrida.getRace());
    }
}
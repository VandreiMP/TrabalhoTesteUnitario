package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.models.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class PistaServiceTest extends BaseTest{

    @Autowired
    SpeedwayService pistaService;

    @Test
    @DisplayName("Teste buscar pista por ID")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void findByIdTest() {
        var pista = pistaService.findById(3);
        assertNotNull(pista);
        assertEquals(3, pista.getId());
        assertEquals("Pista 3", pista.getName());
        assertEquals(15, pista.getSize());
        assertEquals(1, pista.getCountry());
    }

    @Test
    @DisplayName("Teste buscar pista por ID inexistente")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void findByIdNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pistaService.findById(25));
        assertEquals("A pista de ID 25 não existe na base de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir nova pista de corrida")
    void insertSpeedwayTest() {
        Speedway pista = new Speedway(null, "Pista nova teste insert", 100, new Country(1, "Brasil"));
        pistaService.insert(pista);
        pista = pistaService.findById(1);
        assertEquals(1, pista.getId());
        assertEquals("Pista nova teste insert", pista.getName());
    }

    @Test
    @DisplayName("Teste remover pista 1")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void removeSpeedwayTest() {
        pistaService.delete(1);
        List<Speedway> lista = pistaService.listAll();
    }

    @Test
    @DisplayName("Teste remover pista inexistente")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void removeSpeedwayNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pistaService.delete(35));
        assertEquals("A pista de ID 35 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todas as pistas")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void listAllSpeedwayTest() {
        List<Speedway> lista = pistaService.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir pistas cadastradas")
    void listAllSpeedwayEmptyTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pistaService.listAll());
        assertEquals("Nenhuma pista cadastrada", exception.getMessage());
    }

    @Test
    @DisplayName("Teste alterar equipe")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void updateSpeedwayTest() {
        var pista = pistaService.findById(3);
        assertEquals("Pista 3", pista.getName());
        var pistaAlteracao = new Speedway(3,"Pista 3 teste Alteração", 15, new Country(1, "Brasil"));
        pistaService.update(pistaAlteracao);
        pista = pistaService.findById(3);
        assertEquals("Pista 3 teste Alteração", pistaAlteracao.getName());
    }

    @Test
    @DisplayName("Teste alterar pista inexistente")
    void updateSpeedwayNonExistsTest() {
        var pistAlteracao = new Speedway(90,"Pista inexistente", 1, new Country(1, "Brasil"));
        var exception = assertThrows(
                ObjectNotFound.class, () -> pistaService.update(pistAlteracao));
        assertEquals("A equipe 90 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar por pista que contém tamanho entre")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void findBySizeBetweenTest() {
        var lista = pistaService.findBySizeBetween(12, 16);
        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("Teste buscar por pista que contém (ignore case)")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByNameStartsWithIgnoreCaseTest() {
        var lista = pistaService.findByNameStartsWithIgnoreCase("eq");
        assertEquals(2, lista.size());
    }

}

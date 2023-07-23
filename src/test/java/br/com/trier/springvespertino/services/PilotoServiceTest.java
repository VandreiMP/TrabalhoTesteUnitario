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
public class PilotoServiceTest extends BaseTest{

    @Autowired
    PilotService pilotoService;

    @Test
    @DisplayName("Teste buscar piloto por ID")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdTest() {
        var piloto = pilotoService.findById(5);
        assertNotNull(piloto);
        assertEquals(3, piloto.getId());
        assertEquals("Piloto 5", piloto.getName());
        assertEquals(5, piloto.getCountry());
        assertEquals(5, piloto.getTeam());
    }

    @Test
    @DisplayName("Teste buscar piloto por ID inexistente")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByIdNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pilotoService.findById(25));
        assertEquals("O piloto de ID 25 não existe na base de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir novo piloto")
    void insertPilotTest() {
        Pilot piloto = new Pilot(null, "Piloto novo teste insert", new Country(5, "Brasil"), new Team(5, "Equipe 5"));
        pilotoService.insert(piloto);
        piloto = pilotoService.findById(1);
        assertEquals(1, piloto.getId());
        assertEquals("Piloto novo teste insert", piloto.getName());
    }

    @Test
    @DisplayName("Teste remover piloto 1")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void removePilotTest() {
        pilotoService.delete(1);
        List<Pilot> lista = pilotoService.listAll();
    }

    @Test
    @DisplayName("Teste remover piloto inexistente")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void removePilotNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pilotoService.delete(35));
        assertEquals("O piloto de ID 35 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todas os pilotos")
    @Sql({"classpath:/resources/sqls/pista.sql"})
    void listAllPilotTest() {
        List<Pilot> lista = pilotoService.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir piloto cadastrados")
    void listAllPilotEmptyTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> pilotoService.listAll());
        assertEquals("Nenhum piloto cadastrado", exception.getMessage());
    }

    @Test
    @DisplayName("Teste alterar piloto")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void updatePilotTest() {
        var piloto = pilotoService.findById(5);
        assertEquals("Piloto 5", piloto.getName());
        var pilotoAlteracao = new Pilot(5,"piloto 5 teste Alteração", new Country(5, "País 5"), new Team(5, "Equipe 5"));
        pilotoService.update(pilotoAlteracao);
        piloto = pilotoService.findById(5);
        assertEquals("piloto 5 teste Alteração", piloto.getName());
    }

    @Test
    @DisplayName("Teste alterar piloto inexistente")
    void updatePilotNonExistsTest() {
        var pilotoAlteracao = new Pilot(90,"Piloto inexistente",  new Country(1, "Brasil"), new Team(1, "Equipe 1"));
        var exception = assertThrows(
                ObjectNotFound.class, () -> pilotoService.update(pilotoAlteracao));
        assertEquals("O piloto 90 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar por piloto que contém (ignore case)")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByNameStartsWithIgnoreCaseTest() {
        var lista = pilotoService.findByNameStartsWithIgnoreCase("P");
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste buscar por país")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByCountryTest() {
        var lista = pilotoService.findByCountry(new Country(5, "País 5"));
        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("Teste buscar por equipe")
    @Sql({"classpath:/resources/sqls/piloto.sql"})
    void findByTeamTest() {
        var lista = pilotoService.findByTeam(new Team(6, "Equipe 6"));
        assertEquals(1, lista.size());
    }

}

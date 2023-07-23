package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class CampeonatoServiceTest extends BaseTest{

    @Autowired
    ChampionshipService campeonatoService;

    @Test
    @DisplayName("Teste buscar por ID")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByIdTest() {
        var campeonato = campeonatoService.findById(8);
        assertNotNull(campeonato);
        assertEquals(8, campeonato.getId());
        assertEquals("Campeonato 8", campeonato.getDescription());
    }

    @Test
    @DisplayName("Teste buscar por ID inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByIdNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> campeonatoService.findById(25));
        assertEquals("O campeonato de ID 25 não existe na base de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir novo")
    void insertChampionshipTest() {
        Championship campeonato = new Championship(null, "Campeonato novo teste insert", 2005);
        campeonatoService.insert(campeonato);
        campeonato = campeonatoService.findById(1);
        assertEquals(1, campeonato.getId());
        assertEquals("Campeonato novo teste insert", campeonato.getDescription());
    }

    @Test
    @DisplayName("Teste remover campeonato 1")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void removeChampionshipTest() {
        campeonatoService.delete(1);
    }

    @Test
    @DisplayName("Teste remover campeonato inexistente")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void removeChampionshipNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> campeonatoService.delete(35));
        assertEquals("O campeonato de ID 35 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todas")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void listAllChampionshipTest() {
        List<Championship> lista = campeonatoService.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir equipes cadastradas")
    void listAllChampionshipEmptyTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> campeonatoService.listAll());
        assertEquals("Nenhum campeonato cadastrado", exception.getMessage());
    }

    @Test
    @DisplayName("Teste alterar campeonato")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void updateChampionshipTest() {
        var campeonato = campeonatoService.findById(5);
        assertEquals("Campeonato 9", campeonato.getDescription());
        var campeonatoAlteracao = new Championship(9, "Campeonato 9 Teste Alteração", 2000);
        campeonatoService.update(campeonatoAlteracao);
        campeonato = campeonatoService.findById(5);
        assertEquals("Campeonato 9 Teste Alteração", campeonatoAlteracao.getDescription());
    }

    @Test
    @DisplayName("Teste alterar campeonato inexistente")
    void updateChampionshipNonExistsTest() {
        var campeonatoAlteracao = new Championship(90,"Campeonato inexistente", 0);
        var exception = assertThrows(
                ObjectNotFound.class, () -> campeonatoService.update(campeonatoAlteracao));
        assertEquals("O campeonato 90 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar por campeonato que contém o ano")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByfindByYearTest() {
        var lista = campeonatoService.findByYear(2000);
        assertEquals(1, lista.size());
    }

    @Test
    @DisplayName("Teste buscar por campeonato que contém o período entre")
    @Sql({"classpath:/resources/sqls/campeonato.sql"})
    void findByYearBetweenTest() {
        var lista = campeonatoService.findByYearBetween(1990, 2005);
        assertEquals(2, lista.size());
    }

}

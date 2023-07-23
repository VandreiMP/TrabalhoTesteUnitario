package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import br.com.trier.springvespertino.models.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class TeamServiceTest extends BaseTest{

    @Autowired
    TeamService equipeService;

    @Test
    @DisplayName("Teste buscar time por ID")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByIdTest() {
        var equipe = equipeService.findById(3);
        assertNotNull(equipe);
        assertEquals(5, equipe.getId());
        assertEquals("Equipe 5", equipe.getName());
    }

    @Test
    @DisplayName("Teste buscar equipe por ID inexistente")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByIdNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> equipeService.findById(25));
        assertEquals("O time de ID 10 não existe na base de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir nova equipe")
    void insertTeamTest() {
        Team equipe = new Team(null, "Equipe nova teste insert");
        equipeService.salvar(equipe);
        equipe = equipeService.findById(1);
        assertEquals(1, equipe.getId());
        assertEquals("Equipe nova teste insert", equipe.getName());
    }

    @Test
    @DisplayName("Teste remover equipe 1")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void removeTeamTest() {
        equipeService.delete(1);
        List<Team> lista = equipeService.listAll();
    }

    @Test
    @DisplayName("Teste remover equipe inexistente")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void removeTeamNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> equipeService.delete(35));
        assertEquals("A equipe de ID 35 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todas as equipe")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void listAllTeamsTest() {
        List<Team> lista = equipeService.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir equipes cadastradas")
    void listAllTeamsEmptyTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> equipeService.listAll());
        assertEquals("Nenhuma equipe cadastrada", exception.getMessage());
    }

    @Test
    @DisplayName("Teste alterar equipe")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void updateTeamsTest() {
        var equipe = equipeService.findById(5);
        assertEquals("Equipe 5", equipe.getName());
        var equipeAlteracao = new Team(5,"Equipe 5 teste alteração");
        equipeService.update(equipeAlteracao);
        equipe = equipeService.findById(5);
        assertEquals("Equipe 5 teste alteração", equipe.getName());
    }

    @Test
    @DisplayName("Teste alterar usuário inexistente")
    void updateTeamNonExistsTest() {
        var equipeAlteracao = new Team(90,"Usuário inexistente");
        var exception = assertThrows(
                ObjectNotFound.class, () -> equipeService.update(equipeAlteracao));
        assertEquals("A equipe 90 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar por equipe que contém")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByDescriptioContainsTest() {
        var lista = equipeService.findByNameContains("Equipe");
        assertEquals(2, lista.size());
        var exception = assertThrows(
                ObjectNotFound.class, () -> equipeService.findByNameContains("c"));
        assertEquals("Nenhum nome de usuário conté a letra c", exception.getMessage());
    }

    @Test
    @DisplayName("Teste buscar por equipe que contém (ignore case)")
    @Sql({"classpath:/resources/sqls/equipe.sql"})
    void findByDescriptioContainsIgnoreCaseTest() {
        var lista = equipeService.findByNameIgnoreCase("equipe");
        assertEquals(2, lista.size());
    }

}

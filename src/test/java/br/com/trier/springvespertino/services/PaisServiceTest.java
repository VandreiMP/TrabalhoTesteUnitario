package br.com.trier.springvespertino.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import br.com.trier.springvespertino.BaseTest;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import jakarta.transaction.Transactional;

@Transactional
public class PaisServiceTest extends BaseTest{

    @Autowired
    CountryService paisService;

    @Test
    @DisplayName("Teste buscar pais por ID")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdTest() {
        var pais = paisService.findById(5);
        assertNotNull(pais);
        assertEquals(5, pais.getId());
        assertEquals("Páis 5", pais.getName());
    }

    @Test
    @DisplayName("Teste buscar pais por ID inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByIdNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> paisService.findById(25));
        assertEquals("O pais de ID 25 não existe na base de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Teste inserir novo pais")
    void insertCountryTest() {
        Country pais = new Country(null, "Pais novo teste insert");
        paisService.salvar(pais);
        pais = paisService.findById(1);
        assertEquals(1, pais.getId());
        assertEquals("ais novo teste insert", pais.getName());
    }

    @Test
    @DisplayName("Teste remover pais 1")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void removeCountryTest() {
        paisService.delete(1);
        List<Country> lista = paisService.listAll();
    }

    @Test
    @DisplayName("Teste remover pais inexistente")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void removeCountryNonExistsTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> paisService.delete(35));
        assertEquals("O país de ID 35 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste listar todas os países")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void listAllCountryTest() {
        List<Country> lista = paisService.listAll();
        assertEquals(2, lista.size());
    }

    @Test
    @DisplayName("Teste listar todos sem possuir paises cadastrados")
    void listAllCountryEmptyTest() {
        var exception = assertThrows(
                ObjectNotFound.class, () -> paisService.listAll());
        assertEquals("Nenhuma equipe cadastrada", exception.getMessage());
    }

    @Test
    @DisplayName("Teste alterar pais")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void updateCountryTest() {
        var pais = paisService.findById(5);
        assertEquals("País 5", pais.getName());
        var paisAlteracao = new Country(5,"País 5 teste alteração");
        paisService.update(paisAlteracao);
        pais = paisService.findById(5);
        assertEquals("País 5 teste alteração", pais.getName());
    }

    @Test
    @DisplayName("Teste alterar pais inexistente")
    void updateCountryNonExistsTest() {
        var paisAlteracao = new Country(90,"País inexistente");
        var exception = assertThrows(
                ObjectNotFound.class, () -> paisService.update(paisAlteracao));
        assertEquals("O país 90 não existe na base de dados", exception.getMessage());

    }

    @Test
    @DisplayName("Teste buscar por equipe Equals Ignore Case")
    @Sql({"classpath:/resources/sqls/pais.sql"})
    void findByDescriptioContainsTest() {
        var lista = paisService.findByNomeEqualsIgnoreCase("País 5");
        assertEquals(1, lista.size());
    }

}

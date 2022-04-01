package com.example.demo.service;

import com.example.demo.model.Election;
import com.example.demo.repository.ElectionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElectionServiceTest {

    @Mock
    private ElectionRepository repository;

    @Test
    @DisplayName("Teste de listagem")
    void test_FindAll() {
        Election c = new Election();
        Mockito.when(this.repository.findAll()).thenReturn(Arrays.asList(c, c));
        List<Election> electionList = this.createService().findAll();

        assertThat(electionList.isEmpty()).isFalse();
        assertEquals(electionList.size(), 2);
    }

    @Test
    @DisplayName("Teste para Persistência")
    void test_Save() {
        Election e = new Election();
        Mockito.when(this.repository.save(e)).thenReturn(e);

        e = this.createService().save(e);
        assertNotEquals(e, null);
    }

    @Test
    @DisplayName("Teste para deletar por id")
    void test_Delete() {
        long id = 10001L;
        Mockito.when(this.repository.existsById(id)).thenReturn(Boolean.TRUE);

        this.createService().delete(id);
    }

    @Test
    @DisplayName("Teste de Erro ao deletar quando não for encontrado")
    void test_Delete_notFound() {
        long id = 10001L;
        Mockito.when(this.repository.existsById(id)).thenReturn(Boolean.FALSE);

        assertThrows(IllegalArgumentException.class, () -> this.createService().delete(id));
    }

    private ElectionService createService() {
        return new ElectionServiceImpl(this.repository);
    }
}

package com.agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Suite de testes integrados e unitários para a aplicação Agenda.
 * Cobre desde regras de recorrência até lógica de usuários.
 */
public class TesteAgenda {

    @Nested
    @DisplayName("Testes de Recorrência (Interface Repetivel)")
    class TestesRecorrencia {

        private Evento eventoDiario;
        private Evento eventoSemanal;
        private Evento eventoMensal;
        private Evento eventoAnual;
        private Evento eventoUnico;
        private LocalDate dataInicio;

        @BeforeEach
        void setUp() {
            // Quinta-feira, 16 de Julho de 2026
            dataInicio = LocalDate.of(2026, 7, 16);

            eventoDiario = new Evento("Academia", "Treinar", "Diariamente", dataInicio, LocalTime.of(7, 0), LocalTime.of(8, 0));
            eventoSemanal = new Evento("Terapia", "Sessão semanal", "Semanalmente", dataInicio, LocalTime.of(14, 0), LocalTime.of(15, 0));
            eventoMensal = new Evento("Pagar Aluguel", " Boleto", "Mensalmente", dataInicio, null, null);
            eventoAnual = new Evento("Aniversário", "Festa", "Anualmente", dataInicio, null, null);
            eventoUnico = new Evento("Dentista", "Consulta única", "Nunca", dataInicio, LocalTime.of(10, 0), LocalTime.of(11, 0));
        }

        @Test
        @DisplayName("Não deve permitir que nenhum evento ocorra antes da sua data de início")
        void naoDeveOcorrerAntesDoInicio() {
            LocalDate dataAnterior = dataInicio.minusDays(1);
            assertFalse(eventoDiario.ocorreEm(dataAnterior));
            assertFalse(eventoSemanal.ocorreEm(dataAnterior));
            assertFalse(eventoMensal.ocorreEm(dataAnterior));
        }

        @Test
        @DisplayName("Evento diário deve ocorrer em qualquer dia após o início")
        void deveOcorrerDiariamente() {
            assertTrue(eventoDiario.ocorreEm(dataInicio));
            assertTrue(eventoDiario.ocorreEm(dataInicio.plusDays(1)));
            assertTrue(eventoDiario.ocorreEm(dataInicio.plusDays(100)));
        }

        @Test
        @DisplayName("Evento semanal deve ocorrer apenas no mesmo dia da semana")
        void deveOcorrerSemanalmente() {
            // Quinta-feira seguinte (Mesmo dia da semana) -> Deve ocorrer
            assertTrue(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 23)));
            
            // Sexta-feira seguinte (Dia diferente) -> Não deve ocorrer
            assertFalse(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 24)));
        }

        @Test
        @DisplayName("Evento mensal deve ocorrer apenas no mesmo dia do mês")
        void deveOcorrerMensalmente() {
            // Dia 16 do mês seguinte -> Deve ocorrer
            assertTrue(eventoMensal.ocorreEm(LocalDate.of(2026, 8, 16)));
            
            // Dia 17 do mês seguinte -> Não deve ocorrer
            assertFalse(eventoMensal.ocorreEm(LocalDate.of(2026, 8, 17)));
        }

        @Test
        @DisplayName("Evento anual deve ocorrer apenas no mesmo dia e mês")
        void deveOcorrerAnualmente() {
            // 16 de Julho do ano seguinte -> Deve ocorrer
            assertTrue(eventoAnual.ocorreEm(LocalDate.of(2027, 7, 16)));
            
            // 16 de Agosto do ano seguinte -> Não deve ocorrer
            assertFalse(eventoAnual.ocorreEm(LocalDate.of(2027, 8, 16)));
        }

        @Test
        @DisplayName("Evento com repetição 'Nunca' só deve ocorrer no próprio dia de início")
        void deveOcorrerApenasUmaVez() {
            assertTrue(eventoUnico.ocorreEm(dataInicio));
            assertFalse(eventoUnico.ocorreEm(dataInicio.plusDays(1)));
        }
    }

    @Nested
    @DisplayName("Testes de Estrutura de Usuários")
    class TestesUsuario {

        private Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = new Usuario("João Silva", "joao@email.com", "11999999999", "senha123");
        }

        @Test
        @DisplayName("Deve inicializar as listas e mapas de agenda vazios ao criar usuário")
        void deveInicializarAgendaVazia() {
            assertNotNull(usuario.getAgenda());
            assertNotNull(usuario.getAgendaRepetitiva());
            assertTrue(usuario.getAgenda().isEmpty());
            assertTrue(usuario.getAgendaRepetitiva().isEmpty());
        }

        @Test
        @DisplayName("Deve reter os dados cadastrais corretos do usuário")
        void deveManterDadosCadastrais() {
            assertEquals("João Silva", usuario.getNome());
            assertEquals("joao@email.com", usuario.getEmail());
            assertEquals("senha123", usuario.getSenha());
            assertEquals("11999999999", usuario.getTelefone());
        }
    }

    @Nested
    @DisplayName("Testes de Integração e manipulação de Eventos na Agenda")
    class TestesManipulacaoAgenda {

        private Usuario usuario;
        private Evento eventoPontual;

        @BeforeEach
        void setUp() {
            usuario = new Usuario("Maria Souza", "maria@email.com", "11888888888", "senha456");
            eventoPontual = new Evento(
                "Dentista", 
                "Limpeza", 
                "Nunca", 
                LocalDate.of(2026, 7, 16), 
                LocalTime.of(14, 0), 
                LocalTime.of(15, 0)
            );
        }

        @Test
        @DisplayName("Deve permitir adicionar um evento pontual em uma data específica do mapa")
        void deveAdicionarEventoAoMapa() {
            LocalDate data = eventoPontual.getDiaInicio();
            
            // Simula a adição ao mapa de agenda do usuário
            usuario.getAgenda().put(data, new ArrayList<>());
            usuario.getAgenda().get(data).add(eventoPontual);

            assertTrue(usuario.getAgenda().containsKey(data));
            assertEquals(1, usuario.getAgenda().get(data).size());
            assertEquals("Dentista", usuario.getAgenda().get(data).get(0).getNome());
        }

        @Test
        @DisplayName("Deve validar a troca lógica de horários de início e fim")
        void deveValidarSettersDeTempo() {
            eventoPontual.setHoraInicio(LocalTime.of(9, 0));
            eventoPontual.setHoraFim(LocalTime.of(10, 0));

            assertEquals(LocalTime.of(9, 0), eventoPontual.getHoraInicio());
            assertEquals(LocalTime.of(10, 0), eventoPontual.getHoraFim());
            assertEquals("09:00 - 10:00", eventoPontual.getResumoHorario());
        }
    }
}
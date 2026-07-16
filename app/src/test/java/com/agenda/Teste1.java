package com.agenda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Classe de teste unitário para validar o comportamento cronológico
 * e as regras de negócio de eventos da agenda.
 */
public class Teste1 {

    @Test
    @DisplayName("Deve validar corretamente o resumo de horário para eventos de dia inteiro")
    public void deveValidarResumoDiaInteiro() {
        // Criando um evento de dia inteiro (horários nulos)
        Evento eventoDiaInteiro = new Evento(
            "Reunião Anual", 
            "Planejamento estratégico", 
            "Nunca", 
            LocalDate.of(2026, 12, 31), 
            null, 
            null
        );

        assertEquals("Dia inteiro", eventoDiaInteiro.getResumoHorario());
    }

    @Test
    @DisplayName("Deve validar o resumo de horário quando houver horas definidas")
    public void deveValidarResumoComHoras() {
        // Criando um evento com hora marcada
        Evento eventoComHora = new Evento(
            "Almoço", 
            "Almoço com cliente", 
            "Nunca", 
            LocalDate.of(2026, 7, 16), 
            LocalTime.of(12, 0), 
            LocalTime.of(13, 30)
        );

        assertEquals("12:00 - 13:30", eventoComHora.getResumoHorario());
    }

    @Test
    @DisplayName("Deve validar a recorrência semanal de um evento")
    public void deveValidarOcorrenciaSemanal() {
        // Quinta-feira, 16 de Julho de 2026
        LocalDate dataInicio = LocalDate.of(2026, 7, 16); 
        
        Evento eventoSemanal = new Evento(
            "Aula de Inglês", 
            "Foco em conversação", 
            "Semanalmente", 
            dataInicio, 
            LocalTime.of(19, 0), 
            LocalTime.of(20, 0)
        );

        // Uma semana depois (Quinta-feira, 23 de Julho de 2026) -> Deve ocorrer
        assertTrue(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 23)));

        // Um dia depois (Sexta-feira, 17 de Julho de 2026) -> Não deve ocorrer
        assertFalse(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 17)));

        // Uma data anterior ao início do evento -> Não deve ocorrer
        assertFalse(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 9)));
    }
}
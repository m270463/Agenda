package com.agenda;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa um evento de agenda com horários específicos de início e fim.
 * <p>
 * Esta classe herda as características básicas de {@link ItemAgenda} (como nome, descrição, 
 * padrão de repetição e data inicial) e adiciona suporte a restrições temporárias diárias. 
 * Também suporta eventos marcados para o "dia inteiro", representados por horários nulos.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class Evento extends ItemAgenda {

    /**
     * O horário de início do evento. 
     * Pode ser {@code null} caso o compromisso seja de dia inteiro.
     */
    private LocalTime horaInicio;

    /**
     * O horário de término do evento. 
     * Pode ser {@code null} caso o compromisso seja de dia inteiro.
     */
    private LocalTime horaFim;

    /**
     * Constrói uma nova instância de um Evento com todos os atributos especificados.
     *
     * @param nome        O título ou nome do evento.
     * @param descricao   A descrição detalhada do compromisso.
     * @param repeticao   A regra de recorrência do evento (ex: "Nunca", "Diariamente").
     * @param diaInicio   A data calendário em que o evento se inicia ou ocorre.
     * @param horaInicio  O horário de início do compromisso (ou {@code null} para dia inteiro).
     * @param horaFim     O horário de término do compromisso (ou {@code null} para dia inteiro).
     */
    public Evento(String nome, String descricao, String repeticao, LocalDate diaInicio, LocalTime horaInicio, LocalTime horaFim) {
        super(nome, descricao, repeticao, diaInicio);
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    /**
     * Obtém o horário de início registrado para o evento.
     *
     * @return O {@link LocalTime} representando o início, ou {@code null} se for de dia inteiro.
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Define ou atualiza o horário de início do evento.
     *
     * @param horaInicio O novo {@link LocalTime} de início (pode ser {@code null}).
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Obtém o horário de encerramento registrado para o evento.
     *
     * @return O {@link LocalTime} representando o fim, ou {@code null} se for de dia inteiro.
     */
    public LocalTime getHoraFim() {
        return horaFim;
    }

    /**
     * Define ou atualiza o horário de encerramento do evento.
     *
     * @param horaFim O novo {@link LocalTime} de término (pode ser {@code null}).
     */
    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    /**
     * Gera uma representação textual resumida do intervalo de tempo em que o evento ocorre.
     * <p>
     * Sobrescreve o método abstrato da classe base {@link ItemAgenda}. Caso o horário 
     * de início seja nulo, retorna a constante "Dia inteiro". Do contrário, retorna uma 
     * string formatada no padrão {@code HH:mm - HH:mm}.
     * </p>
     *
     * @return Uma {@link String} contendo o intervalo de horas ou a indicação de dia inteiro.
     */
    @Override
    public String getResumoHorario() {
        return horaInicio == null ? "Dia inteiro" : horaInicio + " - " + horaFim;
    }
}
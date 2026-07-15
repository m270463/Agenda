package com.agenda;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento extends ItemAgenda {
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public Evento(String nome, String descricao, String repeticao, LocalDate diaInicio, LocalTime horaInicio, LocalTime horaFim) {
        super(nome, descricao, repeticao, diaInicio);
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public String getResumoHorario() {
        return horaInicio == null ? "Dia inteiro" : horaInicio + " - " + horaFim;
    }
}
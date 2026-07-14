package com.agenda;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento implements Repetivel{
    private String nome;
    private String descricao;
    private LocalDate diaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String repeticao;
    public Evento(String nome,  String descricao, String repeticao,LocalDate diaInicio,  LocalTime horaInicio, LocalTime horaFim) {
        this.nome = nome;
        this.descricao = descricao;
        this.diaInicio = diaInicio;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.repeticao = repeticao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(LocalDate diaInicio) {
        this.diaInicio = diaInicio;
    }

    public String getRepeticao(){
        return this.repeticao;
    }
    public void setRepeticao(String repeticao){
        this.repeticao = repeticao;
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
}
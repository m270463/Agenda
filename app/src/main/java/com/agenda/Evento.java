package com.agenda;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private String nome;
    private String local;
    private String descricao;
    private LocalDate diaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String repeticao;
    public Evento(String nome, String local, String descricao, String repeticao,LocalDate diaInicio,  LocalTime horaInicio, LocalTime horaFim) {
        this.nome = nome;
        this.local = local;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
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
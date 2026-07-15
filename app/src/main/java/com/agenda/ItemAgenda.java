package com.agenda;

import java.time.LocalDate;


public abstract class ItemAgenda implements Repetivel {

    protected String nome;
    protected String descricao;
    protected LocalDate diaInicio;
    protected String repeticao;

    public ItemAgenda(String nome, String descricao, String repeticao, LocalDate diaInicio) {
        this.nome = nome;
        this.descricao = descricao;
        this.repeticao = repeticao;
        this.diaInicio = diaInicio;
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

    @Override
    public LocalDate getDiaInicio() {
        return diaInicio;
    }

    public void setDiaInicio(LocalDate diaInicio) {
        this.diaInicio = diaInicio;
    }

    @Override
    public String getRepeticao() {
        return repeticao;
    }

    public void setRepeticao(String repeticao) {
        this.repeticao = repeticao;
    }

    /**
     * Cada subtipo decide como resumir seu "horário" para exibição
     * (ex.: um Evento mostra "14:00 - 15:00" ou "Dia inteiro").
     */
    public abstract String getResumoHorario();
}
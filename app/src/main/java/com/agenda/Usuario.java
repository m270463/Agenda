package com.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Usuario{
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private Map <LocalDate, ArrayList<Evento>> agenda;
    private ArrayList<Evento> agendaRepetitiva = new ArrayList<>();

    public Usuario(String nome,String email, String telefone, String senha){
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.agenda = new HashMap<>();
    }

    public String getNome(){
        return nome;
    }

    public String getEmail(){
        return email;
    }

    public String getTelefone(){
        return telefone;
    }
    
    public String getSenha(){
        return senha;
    }

    public Map<LocalDate, ArrayList<Evento>> getAgenda(){
        return this.agenda;
    }

    public ArrayList<Evento> getAgendaRepetitiva(){
        return this.agendaRepetitiva;
    }
}
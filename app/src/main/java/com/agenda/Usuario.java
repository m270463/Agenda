package com.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Representa um usuário do sistema de agenda, contendo suas credenciais, dados de contato e dados de compromissos.
 * <p>
 * A classe gerencia o perfil individual do usuário e centraliza a sua respectiva folha de agenda. 
 * Os compromissos são divididos internamente em duas estruturas para otimização de busca e consistência:
 * </p>
 * <ul>
 * <li><b>Agenda Pontual ({@code agenda}):</b> Um mapa associativo chave-valor que indexa listas de eventos 
 * específicos e únicos (com recorrência "Nunca") diretamente à sua respectiva {@link LocalDate}.</li>
 * <li><b>Agenda Recorrente ({@code agendaRepetitiva}):</b> Uma lista dinâmica contendo eventos recorrentes 
 * (diários, semanais, mensais ou anuais) que precisam ter suas ocorrências calculadas sob demanda.</li>
 * </ul>
 * * @author Seu Nome
 * @version 1.0
 */
public class Usuario {
    
    /** O nome completo do usuário. */
    private String nome;
    
    /** O endereço de e-mail do usuário, utilizado também como credencial única de login. */
    private String email;
    
    /** O número de telefone formatado do usuário. */
    private String telefone;
    
    /** A senha de autenticação para acesso à conta. */
    private String senha;
    
    /** * O mapa da agenda do usuário para compromissos pontuais de ocorrência única.
     * Mapeia cada data ({@link LocalDate}) a uma lista contendo os eventos agendados para aquele dia.
     */
    private Map<LocalDate, ArrayList<Evento>> agenda;
    
    /** * A lista contendo todos os eventos que possuem alguma regra de recorrência ou repetição ativa.
     */
    private ArrayList<Evento> agendaRepetitiva = new ArrayList<>();

    private int id;
    /**
     * Constrói uma nova instância de Usuario com as informações de cadastro obrigatórias.
     * <p>
     * O construtor inicializa a agenda pontual como uma estrutura vazia do tipo {@link HashMap}.
     * </p>
     *
     * @param nome     O nome do usuário.
     * @param email    O endereço de e-mail (deve ser único no sistema).
     * @param telefone O telefone de contato formatado.
     * @param senha    A senha para autenticação de acesso.
     */
    public Usuario(String nome, String email, String telefone, String senha) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
        this.agenda = new HashMap<>();
    }

    /**
     * Obtém o nome cadastrado para o usuário.
     *
     * @return Uma {@link String} contendo o nome do usuário.
     */
    public String getNome() {
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Obtém o endereço de e-mail do usuário.
     *
     * @return Uma {@link String} contendo o e-mail cadastrado.
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Obtém o número de telefone de contato do usuário.
     *
     * @return Uma {@link String} com o telefone do usuário.
     */
    public String getTelefone() {
        return telefone;
    }

    public void SetTelefone(String telefone){
        this.telefone = telefone;
    }
    
    /**
     * Obtém a senha de acesso do usuário.
     *
     * @return Uma {@link String} contendo a senha cadastrada.
     */
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    /**
     * Obtém o mapa associativo contendo os compromissos pontuais (únicos) indexados por data.
     * <p>
     * Útil para buscar diretamente e com alta performance (tempo constante em média) todos os 
     * eventos sem repetição agendados para um dia específico.
     * </p>
     *
     * @return Um {@link Map} onde a chave é a data {@link LocalDate} e o valor é uma {@link ArrayList} de {@link Evento}.
     */
    public Map<LocalDate, ArrayList<Evento>> getAgenda() {
        return this.agenda;
    }

    /**
     * Obtém a lista contendo todos os compromissos repetitivos configurados por este usuário.
     * <p>
     * Esta coleção deve ser percorrida e filtrada (normalmente utilizando métodos de verificação 
     * de ocorrência, como o contrato {@code ocorreEm}) para renderizar compromissos repetitivos no calendário.
     * </p>
     *
     * @return Uma {@link ArrayList} contendo as instâncias de {@link Evento} recorrentes.
     */
    public ArrayList<Evento> getAgendaRepetitiva() {
        return this.agendaRepetitiva;
    }


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setAgenda(Map<LocalDate, ArrayList<Evento>> agenda){
        this.agenda = agenda;
    }

    public void setAgendaRepetitiva(ArrayList<Evento> agendaRep){
        this.agendaRepetitiva = agendaRep;
    }
}
package com.agenda;

import java.time.LocalDate;

/**
 * Classe abstrata que serve de base para todos os elementos que podem ser agendados.
 * <p>
 * Implementa a interface {@link Repetivel} e encapsula as características comuns a qualquer 
 * compromisso de agenda, tais como título (nome), descrição detalhada, data de início e a 
 * regra de recorrência associada.
 * </p>
 * <p>
 * Subclasses desta estrutura (como {@link Evento}) devem fornecer detalhes de implementação 
 * específicos, especialmente na definição de seus intervalos ou limites de horário.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public abstract class ItemAgenda implements Repetivel {

    /** O título ou nome do compromisso. */
    protected String nome;

    /** A descrição detalhada ou anotação sobre o compromisso. */
    protected String descricao;

    /** A data do calendário em que o compromisso ocorre ou se inicia. */
    protected LocalDate diaInicio;

    /** A regra ou padrão de repetição do compromisso (ex: "Nunca", "Semanalmente"). */
    protected String repeticao;

    /**
     * Construtor base para inicializar os atributos fundamentais de qualquer item de agenda.
     *
     * @param nome        O título ou nome do item.
     * @param descricao   A descrição textual do compromisso.
     * @param repeticao   A regra de recorrência para repetição do item.
     * @param diaInicio   A data do calendário ({@link LocalDate}) em que o compromisso está agendado.
     */
    public ItemAgenda(String nome, String descricao, String repeticao, LocalDate diaInicio) {
        this.nome = nome;
        this.descricao = descricao;
        this.repeticao = repeticao;
        this.diaInicio = diaInicio;
    }

    /**
     * Obtém o nome ou título do item de agenda.
     *
     * @return Uma {@link String} contendo o nome atual do compromisso.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Altera ou define o nome do item de agenda.
     *
     * @param nome O novo título para o compromisso.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Obtém a descrição detalhada do item de agenda.
     *
     * @return Uma {@link String} contendo as anotações do compromisso.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Altera ou define a descrição do item de agenda.
     *
     * @param descricao A nova descrição para o compromisso.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém a data de início registrada para o compromisso.
     * <p>
     * Sobrescreve o método definido no contrato da interface {@link Repetivel}.
     * </p>
     *
     * @return O {@link LocalDate} contendo a data inicial do compromisso.
     */
    @Override
    public LocalDate getDiaInicio() {
        return diaInicio;
    }

    /**
     * Altera ou define a data de início do item de agenda.
     *
     * @param diaInicio A nova data {@link LocalDate} do compromisso.
     */
    public void setDiaInicio(LocalDate diaInicio) {
        this.diaInicio = diaInicio;
    }

    /**
     * Obtém a regra de recorrência configurada para o compromisso.
     * <p>
     * Sobrescreve o método definido no contrato da interface {@link Repetivel}.
     * </p>
     *
     * @return Uma {@link String} contendo a recorrência (ex: "Diariamente", "Nunca").
     */
    @Override
    public String getRepeticao() {
        return repeticao;
    }

    /**
     * Altera ou define a regra de recorrência do item de agenda.
     *
     * @param repeticao A nova regra de repetição para o compromisso.
     */
    public void setRepeticao(String repeticao) {
        this.repeticao = repeticao;
    }

    /**
     * Retorna uma representação textual resumida do horário do compromisso para exibição em tela.
     * <p>
     * Cada subtipo concreto decide como formatar e resumir seu intervalo de tempo. Por exemplo, 
     * uma implementação de tarefa pode retornar apenas "Sem horário", enquanto uma implementação 
     * de evento retornará "14:00 - 15:00" ou "Dia inteiro".
     * </p>
     *
     * @return Uma {@link String} representando o resumo de tempo associado ao item.
     */
    public abstract String getResumoHorario();
}
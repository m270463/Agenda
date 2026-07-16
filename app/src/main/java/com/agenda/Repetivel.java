package com.agenda;

import java.time.LocalDate;

/**
 * Interface que define o contrato e os comportamentos para elementos
 * recorrentes ou repetitivos na agenda.
 * <p>
 * Qualquer compromisso que possua uma frequência de repetição definida (como
 * tarefas ou eventos) deve
 * implementar esta interface. Ela fornece uma lógica de verificação temporal
 * unificada (através de um método default)
 * para determinar se um determinado item deve ser exibido em uma data
 * específica do calendário.
 * </p>
 * * @author Seu Nome
 * 
 * @version 1.0
 */
public interface Repetivel {

    /**
     * Obtém a regra ou padrão de repetição do item.
     * <p>
     * Os valores esperados geralmente incluem cadeias de caracteres como:
     * {@code "Nunca"}, {@code "Diariamente"}, {@code "Semanalmente"},
     * {@code "Mensalmente"} ou {@code "Anualmente"}.
     * </p>
     *
     * @return Uma {@link String} correspondente ao padrão de recorrência
     *         configurado.
     */
    String getRepeticao();

    /**
     * Obtém a data de início (data de criação ou primeira ocorrência) do
     * compromisso.
     * <p>
     * Esta data serve como o ponto de partida cronológico e âncora de cálculo para
     * todas as repetições futuras.
     * </p>
     *
     * @return O {@link LocalDate} representando a primeira data do item.
     */
    LocalDate getDiaInicio();

    /**
     * Determina se o compromisso recorrente possui uma ocorrência ativa em uma data
     * consultada.
     * <p>
     * Trata-se de um método padrão (<i>default method</i>) que encapsula a
     * inteligência de cálculo cronológico:
     * </p>
     * <ul>
     * <li>Se a data consultada for anterior à data de início do item, ela é
     * automaticamente descartada (retorna {@code false}).</li>
     * <li>Caso contrário, a verificação é realizada utilizando a expressão
     * {@code switch} baseada no padrão de repetição:
     * <ul>
     * <li><b>"Diariamente":</b> Sempre ocorre (retorna {@code true}).</li>
     * <li><b>"Semanalmente":</b> Ocorre se o dia da semana da data consultada for
     * idêntico ao dia da semana da data de início.</li>
     * <li><b>"Mensalmente":</b> Ocorre se o dia do mês da data consultada for igual
     * ao dia do mês de início.</li>
     * <li><b>"Anualmente":</b> Ocorre se tanto o dia quanto o mês coincidirem com a
     * data de início.</li>
     * <li><b>Padrão (Ex: "Nunca"):</b> Retorna {@code false}, indicando que é um
     * evento único não repetitivo.</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param data A data do calendário ({@link LocalDate}) cuja ocorrência do
     *             evento está sendo verificada.
     * @return {@code true} se o compromisso ocorrer ou repetir na data
     *         especificada; {@code false} caso contrário.
     */
    default boolean ocorreEm(LocalDate data) {
        if (data.isBefore(getDiaInicio()))
            return false;

        // Se for exatamente o dia do início, o evento sempre ocorre (independente de
        // repetir ou não)
        if (data.isEqual(getDiaInicio())) {
            return true;
        }

        return switch (getRepeticao()) {
            case "Diariamente" -> true;
            case "Semanalmente" -> data.getDayOfWeek() == getDiaInicio().getDayOfWeek();
            case "Mensalmente" -> data.getDayOfMonth() == getDiaInicio().getDayOfMonth();
            case "Anualmente" -> data.getDayOfMonth() == getDiaInicio().getDayOfMonth()
                    && data.getMonth() == getDiaInicio().getMonth();
            default -> false; // "Nunca"
        };
    }
}
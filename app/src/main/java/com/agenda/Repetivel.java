package com.agenda;

import java.time.LocalDate;

/**
 * Interface que define o contrato e os comportamentos para elementos
 * recorrentes ou repetitivos na agenda.
 * <p>
 * Qualquer compromisso que possua uma frequência de repetição definida (como
 * tarefas ou eventos) deve implementar esta interface. Ela fornece uma lógica 
 * de verificação temporal unificada (através do método padrão {@code ocorreEm})
 * para determinar se um determinado item deve ser exibido em uma data específica do calendário.
 * </p>
 */
public interface Repetivel {

    /**
     * Obtém a regra ou padrão de repetição do item.
     * <p>
     * Os valores esperados no sistema incluem as seguintes cadeias de caracteres:
     * {@code "Nunca"}, {@code "Diariamente"}, {@code "Semanalmente"},
     * {@code "Mensalmente"} ou {@code "Anualmente"}.
     * </p>
     *
     * @return Uma {@link String} correspondente ao padrão de recorrência configurado.
     */
    String getRepeticao();

    /**
     * Obtém a data de início (data de criação ou primeira ocorrência) do compromisso.
     * <p>
     * Esta data serve como o ponto de partida cronológico (âncora) para os cálculos de 
     * todas as repetições futuras.
     * </p>
     *
     * @return O {@link LocalDate} representando a primeira data em que o item acontece.
     */
    LocalDate getDiaInicio();

    /**
     * Determina se o compromisso recorrente deve ocorrer na data consultada.
     * <p>
     * A verificação descarta automaticamente datas anteriores à data de início ({@link #getDiaInicio()}) 
     * e aplica as seguintes regras matemáticas para cada padrão de repetição ({@link #getRepeticao()}):
     * </p>
     * <ul>
     * <li><b>"Nunca":</b> Retorna {@code true} apenas se a data consultada for exatamente igual à data de início.</li>
     * <li><b>"Diariamente":</b> Retorna {@code true} para qualquer data após o início.</li>
     * <li><b>"Semanalmente":</b> Ocorre se o dia da semana (ex: Segunda-feira) da data consultada 
     * for igual ao dia da semana da data de início.</li>
     * <li><b>"Mensalmente":</b> Ocorre se o dia numérico do mês (ex: dia 15) da data consultada 
     * for igual ao dia do mês de início.</li>
     * <li><b>"Anualmente":</b> Ocorre se tanto o dia quanto o mês (ex: 15 de Julho) da data 
     * consultada coincidirem com a data de início.</li>
     * </ul>
     *
     * @param data A data do calendário ({@link LocalDate}) cuja ocorrência do evento está sendo verificada.
     * @return {@code true} se o compromisso ocorrer ou repetir na data especificada; {@code false} caso contrário ou se o padrão for desconhecido.
     */
    default boolean ocorreEm(LocalDate data) {
        if (data.isBefore(getDiaInicio())) {
            return false;
        }

        return switch (getRepeticao()) {
            case "Nunca" -> data.isEqual(getDiaInicio());
            case "Diariamente" -> true;
            case "Semanalmente" -> data.getDayOfWeek() == getDiaInicio().getDayOfWeek();
            case "Mensalmente" -> data.getDayOfMonth() == getDiaInicio().getDayOfMonth();
            case "Anualmente" -> data.getDayOfMonth() == getDiaInicio().getDayOfMonth()
                    && data.getMonth() == getDiaInicio().getMonth();
            default -> false;
        };
    }
}
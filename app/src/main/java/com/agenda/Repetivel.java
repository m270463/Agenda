package com.agenda;

import java.time.LocalDate;

public interface Repetivel {
    String getRepeticao();

    LocalDate getDiaInicio();

    default boolean ocorreEm(LocalDate data) {
        if (data.isBefore(getDiaInicio()))
            return false;
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
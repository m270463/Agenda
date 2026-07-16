package com.agenda;

/**
 * Interface que estabelece o contrato essencial para componentes ou formulários que requerem validação de dados.
 * <p>
 * É implementada principalmente pelas classes controladoras de interface gráfica (como {@code CriacontaController}, 
 * {@code CriaeventoController} e {@code EditEventoController}) para padronizar e garantir que os dados inseridos pelo usuário 
 * estejam consistentes, completos e livres de erros antes de serem processados, salvos ou persistidos no sistema.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public interface Validavel {

    /**
     * Realiza a varredura e validação das informações presentes no componente ou formulário atual.
     * <p>
     * A implementação concreta deste método deve inspecionar os campos de entrada relevantes 
     * (campos de texto, seletores, etc.), gerenciar a exibição de feedbacks visuais de erro 
     * na interface do usuário (GUI) e consolidar o estado final de conformidade do formulário.
     * </p>
     *
     * @return {@code true} se todos os dados do formulário estiverem corretos e em conformidade com as regras de negócio; 
     * {@code false} caso ocorra qualquer inconsistência, preenchimento incorreto ou ausência de dados obrigatórios.
     */
    boolean validar();
}
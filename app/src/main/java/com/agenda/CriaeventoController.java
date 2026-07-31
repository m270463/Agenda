package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller responsável pela tela de criação de novos eventos na agenda.
 * <p>
 * Esta classe herda de {@link controllerEventos} e fornece as regras de validação concretas 
 * necessárias para registrar um compromisso. Se os dados forem válidos, ela cria uma nova instância 
 * de {@link Evento} e a armazena no perfil do usuário autenticado atual, alternando entre eventos 
 * pontuais (associados a uma data específica no mapa de agenda) e repetitivos.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class CriaeventoController extends controllerEventos implements Validavel {
    
    /**
     * Valida os dados de entrada do formulário de criação de evento.
     * <p>
     * O método verifica sequencialmente se:
     * </p>
     * <ul>
     * <li>O título do evento não está em branco.</li>
     * <li>A data de início está preenchida e segue um formato calendário válido (dd/MM/yyyy).</li>
     * <li>Caso não seja um evento de dia inteiro (interruptor desligado), valida os campos de hora inicial 
     * e final, garantindo que o horário final não seja anterior ao inicial.</li>
     * <li>Um tipo de recorrência/repetição foi selecionado no ComboBox.</li>
     * </ul>
     *
     * @return {@code true} se todos os critérios de validação forem satisfeitos; 
     * {@code false} se houver qualquer irregularidade ou inconsistência cronológica.
     */

    /** Botão usado para confirmar a criação do evento. */
    @FXML
    private Button botaoCriar;

    @Override
    public boolean validar() {
        boolean valido = true;
        if (titulo.getText().isBlank()){
            valido = false;
            erroTitulo.setText("*Campo obrigatório!");
            erroTitulo.setVisible(true);
        }
        else{
            erroTitulo.setVisible(false);
        }

        if (diaInicio.getText().isBlank()){
            valido = false;
            erroDiaInicio.setText("*Campo obrigatório!");
            erroDiaInicio.setVisible(true);
        }
        else if (verificaDateTime(diaInicio.getText())){
            erroDiaInicio.setVisible(false);
        }
        else{
            valido = false;
            erroDiaInicio.setText("*Data inválida!");
            erroDiaInicio.setVisible(true);
        }

        // Validações específicas para horários, aplicadas apenas se não for evento de "dia inteiro"
        if (!btnInterruptor.isSelected()){

            if (horaInicio.getText().isBlank()){
                valido = false;
                erroHoraInicio.setText("*Campo obrigatório!");
                erroHoraInicio.setVisible(true);
            }
            else if (verificaLocalTime(horaInicio.getText())){
                erroHoraInicio.setVisible(false);
            }
            else{
                valido = false;
                erroHoraInicio.setText("*Hora inválida!");
                erroHoraInicio.setVisible(true);
            }
            
            if (horaFim.getText().isBlank()){
                valido = false;
                erroHoraFim.setText("*Campo obrigatório!");
                erroHoraFim.setVisible(true);
            }
            else if (verificaLocalTime(horaFim.getText())){
                erroHoraFim.setVisible(false);
            }
            else{
                valido = false;
                erroHoraFim.setText("*Hora inválida!");
                erroHoraFim.setVisible(true);
            }

            // Verifica se a hora de término ocorre antes da hora de início
            if (!horaInicio.getText().isBlank() && !horaFim.getText().isBlank()
            && verificaLocalTime(horaInicio.getText()) && verificaLocalTime(horaFim.getText())){
                

                LocalTime horaInicial = LocalTime.parse(horaInicio.getText());
                LocalTime horaFinal = LocalTime.parse(horaFim.getText());
                if (horaFinal.isBefore(horaInicial)){
                    valido = false;
                    erroHoraFim.setText("*Horários incompatíveis!");
                    erroHoraFim.setVisible(true);
                }
            }
        }

        if (Comborepeticao.getSelectionModel().getSelectedItem() == null){
            valido = false;
            erroComboRepeticao.setText("*Campo obrigatório!");
            erroComboRepeticao.setVisible(true);
        }
        else{
            erroComboRepeticao.setVisible(false);
        }
        return valido;
    }

    /**
     * Processa a confirmação de criação do evento.
     * <p>
     * Se o formulário passar na validação de {@link #validar()}, os campos são convertidos 
     * para seus respectivos tipos de data e hora ({@link LocalDate} e {@link LocalTime}).
     * </p>
     * <p>
     * Caso o evento seja de recorrência única ("Nunca"), ele é inserido no mapeamento de datas 
     * pontuais do usuário ativo. Caso contrário, é adicionado à lista global de eventos recorrentes 
     * ({@code getAgendaRepetitiva()}). Um efeito visual de sucesso é disparado na tela e, 
     * após 1 segundo, a aplicação retorna à tela de visualização do calendário.
     * </p>
     */
    @Override
    public void botaoConfirmar() {
        String title = "";
        LocalDate diaComeco = null;
        LocalTime horaComeco = null;
        LocalTime horaTermino = null;
        String repeticao = "";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String desc = descricao.getText();

        if (validar()){
            title = titulo.getText();
            diaComeco = LocalDate.parse(diaInicio.getText(), formatador);
            repeticao = Comborepeticao.getValue().toString();
            
            if (!btnInterruptor.isSelected()){
                horaTermino = LocalTime.parse(horaFim.getText());
                horaComeco = LocalTime.parse(horaInicio.getText());
            }
            
            Evento evento = new Evento(title, desc, repeticao, diaComeco, horaComeco, horaTermino);
            
            // Separação entre eventos únicos (pontuais) e recorrentes (repetitivos)
            if (evento.getRepeticao().equals("Nunca")){
                App.usuarioaAtivo.getAgenda().putIfAbsent(diaComeco, new ArrayList<>());
                App.usuarioaAtivo.getAgenda().get(diaComeco).add(evento);
            }
            else {
                App.usuarioaAtivo.getAgendaRepetitiva().add(evento);
            }
            App.gerenciadorDados.inserirEvento(App.usuarioaAtivo.getId(), evento);
            confirmacao.setText("Evento criado!");
            confirmacao.setVisible(true);
            
            // Pausa temporizada para exibir feedback de sucesso antes da troca de tela
            PauseTransition pausa = new PauseTransition(Duration.seconds(1));
            pausa.setOnFinished(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) titulo.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    root.applyCss();
                    root.layout();
                    stage.setTitle("Agenda - Login");

                } catch (IOException e) {
                    System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                    e.printStackTrace();
                }
            });
            pausa.play();
        }
        else {
            confirmacao.setVisible(false);
        }
    }
    
    /**
     * Cancela o fluxo de preenchimento atual e retorna o usuário de volta à tela do calendário.
     * Carrega a cena definida pelo FXML {@code calendario.fxml}.
     */
    @Override
    public void botaoVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) titulo.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Login");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }
}
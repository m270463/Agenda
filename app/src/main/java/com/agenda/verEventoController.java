package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * Controller responsável pela tela de visualização diária dos compromissos da agenda.
 * <p>
 * Esta classe gerencia a exibição dinâmica de uma lista de eventos (sejam eles pontuais ou recorrentes) 
 * agendados para um dia específico. Ela popula em tempo de execução um componente {@link GridPane}, 
 * gerando botões clicáveis para cada evento que dão acesso à sua edição, além de permitir 
 * navegar de forma cronológica dia a dia (avançando ou retrocedendo datas) e retornar à visualização do calendário principal.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class verEventoController {
    
    /** Lista local que armazena os eventos que ocorrem na data atualmente visualizada. */
    private ArrayList<Evento> eventosDia = new ArrayList<>();
    
    /** A data do calendário atualmente em foco de visualização nesta tela. */
    private LocalDate dataDia;
    
    /**
     * Inicializa o controller injetando a lista de eventos e a data diária de referência.
     * <p>
     * Este método deve ser invocado imediatamente pelo controller anterior durante a transição 
     * de cenas para carregar e renderizar os dados apropriados na tela de visualização.
     * </p>
     *
     * @param eventosDia A {@link ArrayList} contendo os eventos associados à data informada.
     * @param dataDia    A data {@link LocalDate} de referência deste dia de visualização.
     */
    public void carregarLista(ArrayList<Evento> eventosDia, LocalDate dataDia){
        this.eventosDia = eventosDia;
        this.dataDia = dataDia;
        montarDados();
    }

    /** Label que indica o cabeçalho da tela (exibe a data atual formatada). */
    @FXML 
    private Label eventoDia;

    /** Painel em grade onde os botões dos eventos e seus respectivos horários são alinhados e injetados dinamicamente. */
    @FXML
    private GridPane gridEventos;

    /** Label opcional destinada à sinalização visual de erros na interface. */
    @FXML
    private Label confirmacaoErro;

    /**
     * Renderiza dinamicamente na interface gráfica os eventos carregados na lista.
     * <p>
     * O método formata o título da tela com a data correspondente, limpa ou adiciona restrições de linha 
     * no {@link GridPane} e, para cada evento:
     * </p>
     * <ul>
     * <li>Cria um {@link Button} com o título do evento que, ao ser clicado, carrega a tela de edição/detalhes.</li>
     * <li>Cria um {@link Label} para exibir o horário formatado do compromisso (ou "Dia inteiro").</li>
     * <li>Configura estilos CSS inline e restrições de layout para garantir o preenchimento proporcional.</li>
     * <li>Injeta os componentes em novas linhas do grid e redimensiona a altura total do painel de exibição.</li>
     * </ul>
     */
    private void montarDados(){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        eventoDia.setText(eventoDia.getText() + " " + dataDia.format(formatador));

        int linha = 0;

        for (Evento evento: eventosDia){
            Button botaoDia = new Button();
            Label labelHorario = new Label();
            
            if (evento.getHoraInicio() == null)
                labelHorario.setText("Dia inteiro");
            else
                labelHorario.setText(evento.getHoraInicio() + " - " + evento.getHoraFim());
            
            botaoDia.setText(evento.getNome());

            botaoDia.setMaxWidth(Double.MAX_VALUE);
            botaoDia.setMaxHeight(Double.MAX_VALUE);
            labelHorario.setMaxWidth(Double.MAX_VALUE);
            labelHorario.setMaxHeight(Double.MAX_VALUE);
            
            botaoDia.setStyle("-fx-background-color:  #121212; -fx-text-fill: white; -fx-border-color: #333333");
            labelHorario.setStyle("-fx-background-color:  #121212; -fx-text-fill: white; -fx-border-color: #333333;");
            labelHorario.setAlignment(Pos.CENTER);

            // Abre a tela de edição ao clicar no botão do evento correspondente
            botaoDia.setOnAction(event-> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("editevento.fxml"));
                    Parent root = loader.load();

                    EditEventoController novoController = loader.getController();
                    novoController.montarEvento(evento);
                    Stage stage = (Stage) gridEventos.getScene().getWindow();

                    Scene novaCena = new Scene(root);
                    stage.setScene(novaCena);
                    stage.setTitle("Agenda - Calendário");
                    stage.centerOnScreen();
                    stage.show();

                } catch (IOException e) {
                    System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                    e.printStackTrace();
                }
            });

            RowConstraints rc = new RowConstraints();
            rc.setMinHeight(75);   
            rc.setPrefHeight(75);  
            rc.setMaxHeight(75);
            gridEventos.getRowConstraints().add(linha, rc);
            gridEventos.add(botaoDia, 0, linha);
            gridEventos.add(labelHorario, 1, linha);
            
            gridEventos.setPrefHeight(gridEventos.getPrefHeight() + 75);

            linha += 1;
        }
    }

    /**
     * Avança a data de visualização em exatamente 1 dia.
     * <p>
     * Calcula as ocorrências pontuais e repetitivas do novo dia para o usuário ativo,
     * recarrega a cena {@code verevento.fxml} e injeta a nova listagem atualizada de compromissos.
     * </p>
     */
    @FXML
    private void AvancarDia(){
        LocalDate Data = dataDia.plusDays(1);
        ArrayList<Evento> lista = new ArrayList<>();
        if (App.usuarioaAtivo.getAgenda().get(Data) != null)
            lista.addAll(App.usuarioaAtivo.getAgenda().get(Data));
            
        for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
            if (evento.ocorreEm(Data))
                lista.add(evento);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
            Parent root = loader.load();

            verEventoController novoController = loader.getController();
            novoController.carregarLista(lista, Data);
            Stage stage = (Stage) gridEventos.getScene().getWindow();

            Scene novaCena = new Scene(root);
            stage.setScene(novaCena);
            stage.setTitle("Agenda - Calendário");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Retrocede a data de visualização em exatamente 1 dia.
     * <p>
     * Calcula as ocorrências pontuais e repetitivas do novo dia anterior para o usuário ativo,
     * recarrega a cena {@code verevento.fxml} e injeta a nova listagem atualizada de compromissos.
     * </p>
     */
    @FXML
    private void retardarDia(){
        LocalDate Data = dataDia.minusDays(1);
        ArrayList<Evento> lista = new ArrayList<>();
        if (App.usuarioaAtivo.getAgenda().get(Data) != null)
            lista.addAll(App.usuarioaAtivo.getAgenda().get(Data));
            
        for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
            if (evento.ocorreEm(Data))
                lista.add(evento);
        }       

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
            Parent root = loader.load();

            verEventoController novoController = loader.getController();
            novoController.carregarLista(lista, Data);
            Stage stage = (Stage) gridEventos.getScene().getWindow();

            Scene novaCena = new Scene(root);
            stage.setScene(novaCena);
            stage.setTitle("Agenda - Calendário");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Retorna o fluxo de navegação do usuário de volta para a tela principal do calendário.
     * Carrega a cena declarada em {@code calendario.fxml}.
     */
    @FXML
    private void botaoVoltar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) eventoDia.getScene().getWindow();
            Scene novaCena = new Scene(root);

            stage.setScene(novaCena);
            stage.setTitle("Agenda - Calendário");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }
}
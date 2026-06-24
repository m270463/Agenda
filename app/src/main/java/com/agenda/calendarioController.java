package com.agenda;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class calendarioController {
    @FXML
    private GridPane calendario;

    @FXML   
    private Button voltar;

    @FXML
    private Button avancar;

    @FXML 
    private ComboBox<String> comboMes;

    @FXML
    private ComboBox<String> comboAno;

    

    private YearMonth mesAtual;

    @FXML
    public void initialize() {



        mesAtual = YearMonth.now();
        // colocar alguns códigos aqui como funções em um outro arquivo, para implementar uma interface.

        List<String> meses = new ArrayList<>();
        Locale localBR = new Locale("pt", "BR");
        for (int i = 1; i <= 12; i++) {
            String nomeMes = java.time.Month.of(i).getDisplayName(TextStyle.FULL, localBR);
            nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
            meses.add(nomeMes);
        }
        comboMes.setItems(FXCollections.observableArrayList(meses));
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1);

        comboMes.setButtonCell(criarCelulaCustomizada(false)); 
        comboMes.setCellFactory(lv -> criarCelulaCustomizada(true)); 

        List<String> anos = new ArrayList<>();
        for (int i = 1970; i <= 2050; i++){
            anos.add(String.valueOf(i));
        }

        comboAno.setItems(FXCollections.observableArrayList(anos));
        comboAno.setValue(String.valueOf(mesAtual.getYear()));

        comboAno.setButtonCell(criarCelulaCustomizada(false));
        comboAno.setCellFactory(lv -> criarCelulaCustomizada(true));

        Platform.runLater(() -> {
            Node arrowButton = comboMes.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setStyle("-fx-background-color: #1c1d22; -fx-border-color:  white white white transparent");
            }
            Node arrow = comboMes.lookup(".arrow");
            if (arrow != null) {
                arrow.setStyle("-fx-background-color: white;");
            }

            Node arrowButton1 = comboAno.lookup(".arrow-button");
            if (arrowButton1 != null) {
                arrowButton1.setStyle("-fx-background-color: #1c1d22; -fx-border-color:  white white white transparent");
            }
            Node arrow1 = comboAno.lookup(".arrow");
            if (arrow1 != null) {
                arrow1.setStyle("-fx-background-color: white;");
            }
        });

        comboMes.setOnAction(event -> {
            int mesSelecionado = comboMes.getSelectionModel().getSelectedIndex() + 1;
            mesAtual = YearMonth.of(mesAtual.getYear(), mesSelecionado);
            montarCalendario(mesAtual);
        });

        comboAno.setOnAction(event -> {
            int anoSelecionado = Integer.parseInt(comboAno.getValue());
            mesAtual = YearMonth.of(anoSelecionado, comboMes.getSelectionModel().getSelectedIndex() + 1);
            montarCalendario(mesAtual);
        });



        comboMes.setVisibleRowCount(12);
        comboAno.setVisibleRowCount(10);
        montarCalendario(mesAtual);
    }

    private ListCell<String> criarCelulaCustomizada(boolean celula) {
        return new ListCell<>() {   
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else if (celula){
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-alignment: CENTER; -fx-background-color:  #1c1d22;");
                }
                else{
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-alignment: CENTER; -fx-background-color:  #1c1d22; -fx-border-color:   white  transparent white white");
                }
            }
        };
    }

    
    @FXML
    private void botaoVoltar(){
        mesAtual = mesAtual.minusMonths(1);
        montarCalendario(mesAtual);
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1 % 12);
        comboAno.setValue(String.valueOf(mesAtual.getYear()));
        
    }

    @FXML 
    private void botaovoltarInicio(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) avancar.getScene().getWindow();

        Scene novaCena = new Scene(root);

        stage.setScene(novaCena);
        stage.setTitle("Agenda - Login");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }
    

    @FXML
    private void botaoAvancar(){
        mesAtual = mesAtual.plusMonths(1);
        montarCalendario(mesAtual);
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1 % 12);
        comboAno.setValue(String.valueOf(mesAtual.getYear()));
    }


    private void montarCalendario(YearMonth mesAtual){
        
        calendario.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            int linhaAtual = (rowIndex == null) ? 0 : rowIndex;
            return linhaAtual > 0;
            });


        LocalDate primeirodiaMes = mesAtual.atDay(1);
        int diaSemana = primeirodiaMes.getDayOfWeek().getValue();
        int colunaInicial = 0;
        if (diaSemana != 7)
            colunaInicial = diaSemana;
        
        int totalDias = mesAtual.lengthOfMonth();

        int linha = 1;
        int coluna = colunaInicial;
        for (int dia = 1; dia <= totalDias; dia++){
            StackPane stack = new StackPane();
            stack.setAlignment(Pos.CENTER_LEFT);
            stack.setMaxHeight(Double.MAX_VALUE);
            stack.setMaxWidth(Double.MAX_VALUE);
            stack.setStyle("-fx-background-color: #121212;-fx-border-color: #333333");
            
            Button botaoDia = new Button(String.valueOf(dia));

            botaoDia.setMaxWidth(Double.MAX_VALUE);
            botaoDia.setMaxHeight(Double.MAX_VALUE);
            // Define a cor da borda (linha do grid), a espessura, o fundo do botão e a cor do texto
            botaoDia.setStyle(
                "-fx-border-color: transparent; " +    // Cor da linha (um cinza elegante para contrastar com o preto)
                "-fx-border-width: 0.5px; " +       // Espessura da linha
                "-fx-background-color: transparent; " + // Cor de fundo do botão (um preto levemente mais claro que o fundo)
                "-fx-text-fill: white;"  +           // Cor do número do dia (branco)
                "-fx-alignment: top-left; " +       // Alinha o texto no canto superior esquerdo
                "-fx-padding: 4px 0px 0px 6px;"     // Dá uma folga de 4px do topo e 6px da esquerda
            );

            LocalDate dataBotao = mesAtual.atDay(dia);

            if (dataBotao.isEqual(LocalDate.now())) {
                stack.setStyle(
                    "-fx-border-color: #00adb5; " +    
                    "-fx-border-width: 1.5px; " +       
                    "-fx-background-color: #1a1a1a; " 
                );
        }
            stack.getChildren().add(botaoDia);
            ArrayList<Evento> eventosDia = new ArrayList<>();
            if (App.usuarioaAtivo.getAgenda().get(dataBotao) != null)
                eventosDia.addAll(App.usuarioaAtivo.getAgenda().get(dataBotao));
            

            for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (!dataBotao.isBefore(evento.getDiaInicio()))   {

                    if (evento.getRepeticao().equals("Diariamente"))
                        eventosDia.add(evento);

                    else if (evento.getRepeticao().equals("Semanalmente") && dataBotao.getDayOfWeek() == evento.getDiaInicio().getDayOfWeek())
                        eventosDia.add(evento);
                    else if (evento.getRepeticao().equals("Mensalmente") && evento.getDiaInicio().getDayOfMonth() == dia)  
                        eventosDia.add(evento);
                        
                }
            }

            if (!eventosDia.isEmpty()){
                Label badgeContador = new Label(String.valueOf(eventosDia.size()));
                badgeContador.setStyle(
                    "-fx-background-color: #FF0000; " + 
                    "-fx-text-fill: white; " +         
                    "-fx-font-size: 9px; " +           
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 2px 5px 2px 5px; " +  
                    "-fx-background-radius: 10px;"+
                    "fx-alignment: top-right"      
                );
                stack.getChildren().add(badgeContador);    
                stack.setAlignment(badgeContador, Pos.TOP_RIGHT);
            }
            
            final ArrayList<Evento> eventosNoDia = eventosDia;
            final LocalDate dataDia = dataBotao;
            final int diaSelecionado = dia;
            botaoDia.setOnAction(event ->{

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                Parent root = loader.load();

                verEventoController novoController = loader.getController();
                novoController.carregarLista(eventosNoDia,dataDia);
                Stage stage = (Stage) comboMes.getScene().getWindow();

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

        calendario.add(stack, coluna, linha);

        coluna++;

            if (coluna > 6){
                coluna = 0;
                linha++;
            }
        }

    }
    @FXML
    private void botaoAdicionar(){
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("criaevento.fxml"));
    Parent root = loader.load();

    Stage stage = (Stage) comboAno.getScene().getWindow();

    Scene novaCena = new Scene(root);

    stage.setScene(novaCena);
    stage.setTitle("Agenda - Novo Evento");
    stage.centerOnScreen();
    stage.show();

    } catch (IOException e) {
        System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
        e.printStackTrace();
    }
    }

}
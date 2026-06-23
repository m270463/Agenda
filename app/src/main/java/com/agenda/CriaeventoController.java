package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import org.controlsfx.control.ToggleSwitch;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CriaeventoController {
    @FXML
    private TextField titulo;

    @FXML
    private Label erroTitulo;

    @FXML
    private TextField local;

    @FXML
    private TextField diaInicio;

    @FXML
    private Label erroDiaInicio;

    @FXML
    private TextField horaInicio;

    @FXML
    private Label erroHoraInicio;

    @FXML
    private TextField horaFim;

    @FXML
    private Label erroHoraFim;

    @FXML
    private TextField descricao;

    @FXML
    private ToggleSwitch btnInterruptor;

    @FXML
    private ComboBox Comborepeticao;

    @FXML
    private Label erroComboRepeticao;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Label confirmacao;

    @FXML
    private void initialize(){
        Platform.runLater(() -> anchor.requestFocus());
        ArrayList<String> repeticoes = new ArrayList<>();
        repeticoes.add("Nunca");
        repeticoes.add("Diariamente");
        repeticoes.add("Semanalmente");
        repeticoes.add("Mensalmente");
        repeticoes.add("Anualmente");
        Comborepeticao.setItems(FXCollections.observableArrayList(repeticoes));
        Comborepeticao.setButtonCell(criarCelulaCustomizada()); 
        Comborepeticao.setCellFactory(lv -> criarCelulaCustomizada()); 
        Platform.runLater(() -> {
            Node arrowButton = Comborepeticao.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setStyle("-fx-background-color: #1c1d22; -fx-border-color: transparent");
            }
            Node arrow = Comborepeticao.lookup(".arrow");
            if (arrow != null) {
                arrow.setStyle("-fx-background-color: white;");
            }
        });
        Comborepeticao.setStyle("-fx-prompt-text-fill: white; -fx-border-color: white;");

    }

   private ListCell<String> criarCelulaCustomizada() {
        return new ListCell<>() {   
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-text-fill: white;  -fx-background-color:  #1c1d22; -fx-border-color:  transparent ;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-alignment: CENTER; -fx-background-color:  #1c1d22;-fx-control-inner-background: #121212; ");
                }
            }
        };
    }

    @FXML
    private void interruptor(){
        boolean ativou = false;
        if (btnInterruptor.isSelected()){
            horaInicio.setManaged(false);
            horaFim.setManaged(false);
            horaInicio.setVisible(false);
            horaFim.setVisible(false);
            erroHoraInicio.setVisible(false);
            erroHoraFim.setVisible(false);
            ativou = true;
            erroComboRepeticao.setTranslateY(-100);
            confirmacao.setTranslateY(-110);
        }
        else{
            horaFim.setManaged(true);
            horaInicio.setManaged(true);
            horaInicio.setVisible(true);
            horaFim.setVisible(true);
            erroComboRepeticao.setTranslateY(0);
            confirmacao.setTranslateY(0);
        }
    horaInicio.getParent().requestLayout();
    }

    private boolean verificaDateTime(String dateTime){
        DateTimeFormatter formatador = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/uuuu")
                .toFormatter()
                .withChronology(IsoChronology.INSTANCE)
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            LocalDate.parse(dateTime, formatador);
            return true;
            
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean verificaLocalTime(String localTime){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.SMART);
        try{
            LocalTime.parse(localTime,formatador);
            return true;
        }catch(DateTimeParseException e){
            return false;
        }

    }



    @FXML
    private void botaoCadastro(){
        boolean valido = true;
        String title = "";
        LocalDate diaComeco= null;
        LocalTime horaComeco = null;
        LocalTime horaTermino = null;
        String repeticao = "";

        if (titulo.getText().isBlank()){
            valido = false;
            erroTitulo.setText("*Campo obrigatório!");
            erroTitulo.setVisible(true);
        }
        else{
            erroTitulo.setVisible(false);
            title = titulo.getText();
        }

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (diaInicio.getText().isBlank()){
            valido = false;
            erroDiaInicio.setText("*Campo obrigatório!");
            erroDiaInicio.setVisible(true);
        }

        else if (verificaDateTime(diaInicio.getText())){
            erroDiaInicio.setVisible(false);
            diaComeco = LocalDate.parse(diaInicio.getText(), formatador);
        }

        else{
            valido = false;
            erroDiaInicio.setText("*Data inválida!");
            erroDiaInicio.setVisible(true);
        }

        if (!btnInterruptor.isSelected()){

            if (horaInicio.getText().isBlank()){
                valido = false;
                erroHoraInicio.setText("*Campo obrigatório!");
                erroHoraInicio.setVisible(true);
            }

            else if (verificaLocalTime(horaInicio.getText())){
                erroHoraInicio.setVisible(false);
                horaComeco = LocalTime.parse(horaInicio.getText());
            }

            else{
                valido = false;
                erroHoraInicio.setText("*Data inválida!");
                erroHoraInicio.setVisible(true);
            }
            
            if (horaFim.getText().isBlank()){
                valido = false;
                erroTitulo.setText("*Campo obrigatório!");
                erroTitulo.setVisible(true);
            }

            else if (verificaLocalTime(horaFim.getText())){
                erroTitulo.setVisible(false);
                horaTermino = LocalTime.parse(horaFim.getText());
            }

            else{
                valido = false;
                erroTitulo.setText("*Data inválida!");
                erroTitulo.setVisible(true);
            }
        }

        if (Comborepeticao.getSelectionModel().getSelectedItem() == null){
            valido = false;
            erroComboRepeticao.setText("*Campo obrigatório!");
            erroComboRepeticao.setVisible(true);
        }
        else{
            erroComboRepeticao.setVisible(false);
            repeticao = Comborepeticao.getValue().toString();
        }

        String desc = descricao.getText();

        if (valido){
            Evento evento = new Evento(title, desc, repeticao, diaComeco, horaComeco, horaTermino);
            LocalDate dataAtual = diaComeco;
            if (evento.getRepeticao().equals("NUNCA")){
                App.usuarioaAtivo.getAgenda().putIfAbsent(diaComeco, new ArrayList<>());
                App.usuarioaAtivo.getAgenda().get(diaComeco).add(evento);
            }
            else
                App.usuarioaAtivo.getAgendaRepetitiva().add(evento);

            confirmacao.setText("Evento criado!");
            confirmacao.setVisible(true);

        }
        else{
            confirmacao.setVisible(false);
            return;
        }
    }
    
    @FXML
    private void botaoVoltar(){
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) titulo.getScene().getWindow();

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
}


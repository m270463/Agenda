package com.agenda;

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
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

public abstract class controllerEventos implements Validavel{
    @FXML
    protected TextField titulo;

    @FXML
    protected Label erroTitulo;

    @FXML
    protected TextField local;

    @FXML
    protected TextField diaInicio;

    @FXML
    protected Label erroDiaInicio;

    @FXML
    protected TextField horaInicio;

    @FXML
    protected Label erroHoraInicio;

    @FXML
    protected TextField horaFim;

    @FXML
    protected Label erroHoraFim;

    @FXML
    protected TextField descricao;

    @FXML
    protected ToggleSwitch btnInterruptor;

    @FXML
    protected ComboBox Comborepeticao;

    @FXML
    protected Label erroComboRepeticao;

    @FXML
    protected AnchorPane anchor;

    @FXML
    protected Label confirmacao;

    

    @FXML
    protected void initialize(){
        Platform.runLater(() -> anchor.requestFocus());
        barrasAutomaticas(diaInicio);
        doisPontosAutomaticos(horaInicio);
        doisPontosAutomaticos(horaFim);

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

   protected ListCell<String> criarCelulaCustomizada() {
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
    protected void interruptor(){
        boolean ativou = false;
        if (btnInterruptor.isSelected()){
            horaInicio.setManaged(false);
            horaFim.setManaged(false);
            horaInicio.setVisible(false);
            horaFim.setVisible(false);
            erroHoraInicio.setVisible(false);
            erroHoraFim.setVisible(false);
            ativou = true;
        }
        else{
            horaFim.setManaged(true);
            horaInicio.setManaged(true);
            horaInicio.setVisible(true);
            horaFim.setVisible(true);
        }
    horaInicio.getParent().requestLayout();
    }

    protected boolean verificaDateTime(String dateTime){
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

    protected boolean verificaLocalTime(String localTime){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.SMART);
        try{
            LocalTime.parse(localTime,formatador);
            return true;
        }catch(DateTimeParseException e){
            return false;
        }

    }

    protected void barrasAutomaticas(TextField campoData){
        campoData.setTextFormatter(new TextFormatter<>(change -> {
        if (change.isDeleted() || change.getText().isEmpty() || change.getText().length() == 10) {
            return change;
        }

    if (!change.getText().matches("[0-9]*")) {
        return null;
    }

    String textoFuturo = change.getControlNewText();

    if (textoFuturo.length() > 10) {
        return null;
    }

    if (textoFuturo.length() == 2 || textoFuturo.length() == 5) {
        change.setText(change.getText() + "/");
        int novaPosicao = change.getCaretPosition() + 1;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    return change;
    }));
    }

protected void doisPontosAutomaticos(TextField campoHora){
    campoHora.setTextFormatter(new TextFormatter<>(change -> {
    if (change.isDeleted() || change.getText().isEmpty() || change.getText().length() == 5) {
        return change;
    }

    if (!change.getText().matches("[0-9]*")) {
        return null;
    }

    String textoFuturo = change.getControlNewText();

    if (textoFuturo.length() > 5) {
        return null;
    }

    if (textoFuturo.length() == 2) {
        change.setText(change.getText() + ":");
        int novaPosicao = change.getCaretPosition() + 1;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    return change;
    }));
    }

    @FXML
    public abstract void botaoVoltar();

    @FXML
    public abstract void botaoConfirmar();
}

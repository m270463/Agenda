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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EditEventoController extends controllerEventos implements Validavel{
    private Evento evento;
    
    @FXML
    private Button confirmar;
    
    @FXML
    private VBox vbox;
    
    @FXML
    private AnchorPane anchorRemover;

    @FXML
    private Label confirmacao;


    @FXML
    private Label confirmacaoErro;

    @FXML
    private ToggleButton botaoEdicao;

   
  
    public void montarEvento(Evento evento){
        this.evento = evento;

        titulo.setText(evento.getNome());
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        diaInicio.setText(evento.getDiaInicio().format(formatadorData));
        if (evento.getHoraInicio() == null){
            btnInterruptor.setSelected(true);
            horaInicio.setManaged(false);
            horaFim.setManaged(false);
            horaInicio.setVisible(false);
            horaFim.setVisible(false);
            erroHoraInicio.setVisible(false);
            erroHoraFim.setVisible(false);

        }
        else{
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");
            horaInicio.setText(evento.getHoraInicio().format(formatadorHora));
            horaFim.setText(evento.getHoraFim().format(formatadorHora));
        }
        descricao.setText(evento.getDescricao());
        Comborepeticao.getSelectionModel().select(evento.getRepeticao());
        interruptorEdicao();
    }

    @Override
    public boolean validar(){
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

    @Override
    public void botaoConfirmar(){
        String title = "";
        LocalDate diaComeco= null;
        LocalTime horaComeco = null;
        LocalTime horaTermino = null;
        String repeticao = "";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String desc = descricao.getText();

        if (validar()){
            title = titulo.getText();
            diaComeco = LocalDate.parse(diaInicio.getText(), formatador);
            if (!btnInterruptor.isSelected()){
                horaComeco = LocalTime.parse(horaInicio.getText());
                horaTermino = LocalTime.parse(horaFim.getText());
            }
            repeticao = Comborepeticao.getValue().toString();

            evento.setNome(title);
            evento.setDescricao(desc);
            evento.setDiaInicio(diaComeco);
            evento.setHoraInicio(horaComeco);
            evento.setHoraFim(horaTermino);
            if (evento.getRepeticao().equals("Nunca") && !repeticao.equals(evento.getRepeticao())){
                App.usuarioaAtivo.getAgendaRepetitiva().add(evento);
                App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).remove(evento);
            }
            else if (!evento.getRepeticao().equals("Nunca") && repeticao.equals("Nunca")){
                App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).add(evento);
                App.usuarioaAtivo.getAgendaRepetitiva().remove(evento);
            }
            evento.setRepeticao(repeticao);
            confirmacao.setText("Evento editado!");
            confirmacao.setVisible(true);
            PauseTransition pausa = new PauseTransition(Duration.seconds(1));
            pausa.setOnFinished(event ->{
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                    Parent root = loader.load();

                    verEventoController novoController = loader.getController();
                    ArrayList<Evento> lista = new ArrayList<>();
                    if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) != null){
                        lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
                    }
                    for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                        if (e.ocorreEm(evento.getDiaInicio()))
                            lista.add(e);
                    }

                    novoController.carregarLista(lista,evento.getDiaInicio());
                    Stage stage = (Stage) titulo.getScene().getWindow();

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
            pausa.play();
        }
        else{
            confirmacao.setVisible(false);
            return;
        }
    }
    
    @Override
    public void botaoVoltar(){

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                Parent root = loader.load();

                verEventoController novoController = loader.getController();
                ArrayList<Evento> lista = new ArrayList<>();
                if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) != null){
                    lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
                }
                for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                    if (e.ocorreEm(evento.getDiaInicio()))
                        lista.add(e);
                }

                novoController.carregarLista(lista,evento.getDiaInicio());
                Stage stage = (Stage) titulo.getScene().getWindow();

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

    @FXML
    private void botaoRemover(){
        anchorRemover.setVisible(true);
    }
    @FXML
    private void botaoCancelar(){
        anchorRemover.setVisible(false);
    }

    @FXML
    private void botaoConfirmarRemocao(){
        
        if (evento.getRepeticao().equals("Nunca")){
            App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).remove(evento);
        }
        else{
            App.usuarioaAtivo.getAgendaRepetitiva().remove(evento);
        }
        confirmacaoErro.setVisible(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(1));

        pausa.setOnFinished(event ->{
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
            Parent root = loader.load();
            verEventoController novoController = loader.getController();
            
            ArrayList<Evento> lista = new ArrayList<>();
            if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) !=null){
                lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
            }
            for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (e.ocorreEm(evento.getDiaInicio()))
                    lista.add(e);
            }       

            novoController.carregarLista(lista,evento.getDiaInicio());

            Stage stage = (Stage) titulo.getScene().getWindow();

            stage.getScene().setRoot(root);
            
            stage.setTitle("Agenda - Visualização");


            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        });
        pausa.play();
        
    }
    @FXML
    private void interruptorEdicao(){
        if (!botaoEdicao.isSelected()){
            vbox.setDisable(true);
            descricao.setDisable(true);
            confirmar.setDisable(true);
        }
        else{
            vbox.setDisable(false);
            descricao.setDisable(false);
            confirmar.setDisable(false);
        }
    } 
  
}




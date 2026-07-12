package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CriaeventoController extends controllerEventos implements Validavel{
    
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

            if (!horaInicio.getText().isBlank() && !horaFim.getText().isBlank()){
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
        boolean valido = true;
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
            repeticao = Comborepeticao.getValue().toString();
            if (!btnInterruptor.isSelected()){
                horaTermino = LocalTime.parse(horaFim.getText());
                horaComeco = LocalTime.parse(horaInicio.getText());
            }
            Evento evento = new Evento(title, desc, repeticao, diaComeco, horaComeco, horaTermino);
            LocalDate dataAtual = diaComeco;
            if (evento.getRepeticao().equals("Nunca")){
                App.usuarioaAtivo.getAgenda().putIfAbsent(diaComeco, new ArrayList<>());
                App.usuarioaAtivo.getAgenda().get(diaComeco).add(evento);
            }
            else
                App.usuarioaAtivo.getAgendaRepetitiva().add(evento);

            confirmacao.setText("Evento criado!");
            confirmacao.setVisible(true);
            PauseTransition pausa = new PauseTransition(Duration.seconds(1));
            pausa.setOnFinished(event -> {
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


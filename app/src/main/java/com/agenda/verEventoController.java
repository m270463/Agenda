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

public class verEventoController {
    
    private ArrayList<Evento> eventosDia = new ArrayList<>();
    private LocalDate dataDia;
    
    public void carregarLista(ArrayList<Evento> eventosDia,LocalDate dataDia){
        this.eventosDia = eventosDia;
        this.dataDia = dataDia;
        montarDados();

    }

    @FXML 
    private Label eventoDia;

    @FXML
    private GridPane gridEventos;

    @FXML
    private Label confirmacaoErro;


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
            gridEventos.add(botaoDia, 0,linha);
            gridEventos.add(labelHorario, 1,linha);
            
            gridEventos.setPrefHeight(gridEventos.getPrefHeight() + 75);

            
            linha+=1;

        }
    }

    @FXML
    private void AvancarDia(){
        LocalDate Data = dataDia.plusDays(1);
        ArrayList lista = new ArrayList<>();
        if (App.usuarioaAtivo.getAgenda().get(Data) != null)
            lista.addAll(App.usuarioaAtivo.getAgenda().get(Data));
            
            for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (!Data.isBefore(evento.getDiaInicio())){

                    if (evento.getRepeticao().equals("Diariamente"))
                        lista.add(evento);

                    else if (evento.getRepeticao().equals("Semanalmente") && Data.getDayOfWeek() == evento.getDiaInicio().getDayOfWeek())
                        lista.add(evento);
                    else if (evento.getRepeticao().equals("Mensalmente") && evento.getDiaInicio().getDayOfMonth() == Data.getDayOfMonth())  
                        lista.add(evento);
                        
                }
            }

                     try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                Parent root = loader.load();

                verEventoController novoController = loader.getController();
                novoController.carregarLista(lista,Data);
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

    @FXML
    private void retardarDia(){
        LocalDate Data = dataDia.minusDays(1);
        ArrayList lista = new ArrayList<>();
        if (App.usuarioaAtivo.getAgenda().get(Data) != null)
            lista.addAll(App.usuarioaAtivo.getAgenda().get(Data));
            
            for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (!Data.isBefore(evento.getDiaInicio())){

                    if (evento.getRepeticao().equals("Diariamente"))
                        lista.add(evento);

                    else if (evento.getRepeticao().equals("Semanalmente") && Data.getDayOfWeek() == evento.getDiaInicio().getDayOfWeek())
                        lista.add(evento);
                    else if (evento.getRepeticao().equals("Mensalmente") && evento.getDiaInicio().getDayOfMonth() == Data.getDayOfMonth())  
                        lista.add(evento);
                        
                }
            }

                     try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                Parent root = loader.load();

                verEventoController novoController = loader.getController();
                novoController.carregarLista(lista,Data);
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

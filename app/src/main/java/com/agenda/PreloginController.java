package com.agenda;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PreloginController {

    @FXML
    private Button botaoEntrar;

    @FXML
    private void aoClicarBotaoEntrar(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) botaoEntrar.getScene().getWindow();

        Scene novaCena = new Scene(root);

        stage.setScene(novaCena);
        stage.setTitle("Agenda - Principal");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    @FXML
    private void aoClicarBotaoCriar(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("criarconta.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) botaoEntrar.getScene().getWindow();

        Scene novaCena = new Scene(root);

        stage.setScene(novaCena);
        stage.setTitle("Agenda - Principal");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }


}

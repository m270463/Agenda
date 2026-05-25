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
        // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
        Parent root = loader.load();

        // 2. Pega a janela atual (Stage)
        Stage stage = (Stage) botaoEntrar.getScene().getWindow();

        // 3. Cria a nova cena (Garante que Scene está com 'C')
        Scene novaCena = new Scene(root);

        // 4. Configura e mostra a nova janela
        stage.setScene(novaCena);
        stage.setTitle("Agenda - Principal");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            // Se o arquivo teste2.fxml sumir ou estiver com erro, o Java avisa aqui sem travar o programa
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    @FXML
    private void aoClicarBotaoCriar(){
        try {
        // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("criarconta.fxml"));
        Parent root = loader.load();

        // 2. Pega a janela atual (Stage)
        Stage stage = (Stage) botaoEntrar.getScene().getWindow();

        // 3. Cria a nova cena (Garante que Scene está com 'C')
        Scene novaCena = new Scene(root);

        // 4. Configura e mostra a nova janela
        stage.setScene(novaCena);
        stage.setTitle("Agenda - Principal");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            // Se o arquivo teste2.fxml sumir ou estiver com erro, o Java avisa aqui sem travar o programa
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }


}

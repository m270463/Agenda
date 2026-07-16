package com.agenda;
import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Validavel{

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Label textoErro;

    @FXML
    private AnchorPane anchor;
    

    @FXML
    public void initialize() {


        Platform.runLater(() -> anchor.requestFocus());
        campoUsuario.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoSenha.requestFocus(); 
                event.consume(); 
            }
        });

        campoSenha.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                aoClicarBotaoLogin(); 
                event.consume();
            }
        });
}

    @Override
    public boolean validar(){
        for (int i = 0; i < App.listaUsuarios.size(); i++){
            if (App.listaUsuarios.get(i).getEmail().equals(campoUsuario.getText()) && App.listaUsuarios.get(i).getSenha().equals(campoSenha.getText())){
                    App.usuarioaAtivo = App.listaUsuarios.get(i);
                return true;
            }
        }
        return false;
    }

    @FXML
    private void aoClicarBotaoLogin() {
        if (validar()) {
                try {
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoUsuario.getScene().getWindow();

                Scene novaCena = new Scene(root);

                stage.setScene(novaCena);
                stage.setTitle("Agenda - Calendário");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        } else {
            textoErro.setVisible(true);
        }
    }

    @FXML
    private void aoClicarBotaoCriarConta(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("criarconta.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) campoSenha.getScene().getWindow();

            stage.getScene().setRoot(root);
            
            stage.setTitle("Agenda - Criação de Conta");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }

    }

}




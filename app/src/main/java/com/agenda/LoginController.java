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
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Label textoErro;



    @FXML
    public void initialize() {
        Platform.runLater(() ->campoUsuario.requestFocus());
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


    private boolean verificaUsuario(String email, String senha,boolean login){
        for (int i = 0; i < App.listaUsuarios.size(); i++){
            if (App.listaUsuarios.get(i).getEmail().equals(email) && App.listaUsuarios.get(i).getSenha().equals(senha)){
                if (login)
                    App.usuarioaAtivo = App.listaUsuarios.get(i);
                return true;
            }
        }
        return false;
    }


    @FXML
    private void aoClicarBotaoLogin() {
        String usuario = campoUsuario.getText();
        String senha = campoSenha.getText();

        if (verificaUsuario(usuario, senha,true)) {
                try {
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoUsuario.getScene().getWindow();

                Scene novaCena = new Scene(root);

                stage.setScene(novaCena);
                stage.setTitle("Agenda - Principal");
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
    private void aoClicarBotaoVoltar(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("prelogin.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) campoUsuario.getScene().getWindow();

        Scene novaCena = new Scene(root);

        stage.setScene(novaCena);
        stage.setTitle("Agenda - Início");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }

    }

}




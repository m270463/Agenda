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

    // 1. Vincula as variáveis com os componentes do Scene Builder
    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private Label textoErro;



    @FXML
    public void initialize() {
        Platform.runLater(() ->campoUsuario.requestFocus());

        // 1. Quando apertar Enter no campo de Usuário, pula para a Senha
        campoUsuario.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoSenha.requestFocus(); // Dá o foco (joga o cursor) para o campo de senha
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });

        // 2. Quando apertar Enter no campo de Senha, aí sim dispara o Login
        campoSenha.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                aoClicarBotaoLogin(); // Chama direto a sua função de validação!
                event.consume();
            }
        });
}


    private boolean verificaUsuario(String email, String senha){
        for (int i = 0; i < App.listaUsuarios.size(); i++){
            if (App.listaUsuarios.get(i).getEmail().equals(email) && App.listaUsuarios.get(i).getSenha().equals(senha))
                return true;
        }
        return false;
    }


    // 2. A função que o botão vai chamar ao ser clicado
    @FXML
    private void aoClicarBotaoLogin() {
        String usuario = campoUsuario.getText();
        String senha = campoSenha.getText();

        // Validação simples (depois você pode puxar do banco de dados)
        if (verificaUsuario(usuario, senha)) {
                try {
                // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
                Parent root = loader.load();

                // 2. Pega a janela atual (Stage)
                Stage stage = (Stage) campoUsuario.getScene().getWindow();

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
        } else {
            // Se errar, exibe a mensagem de erro na tela
            textoErro.setVisible(true);
        }
    }

    @FXML
    private void aoClicarBotaoVoltar(){
        try {
        // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("prelogin.fxml"));
        Parent root = loader.load();

        // 2. Pega a janela atual (Stage)
        Stage stage = (Stage) campoUsuario.getScene().getWindow();

        // 3. Cria a nova cena (Garante que Scene está com 'C')
        Scene novaCena = new Scene(root);

        // 4. Configura e mostra a nova janela
        stage.setScene(novaCena);
        stage.setTitle("Agenda - Início");
        stage.centerOnScreen();
        stage.show();

        } catch (IOException e) {
            // Se o arquivo teste2.fxml sumir ou estiver com erro, o Java avisa aqui sem travar o programa
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }

    }

}




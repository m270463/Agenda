package com.agenda;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CriacontaController {

    @FXML
    private Button botaoVoltar;
    
    @FXML
    private TextField campoNome;

    @FXML
    private Label erroNome;

    @FXML
    private TextField campoEmail;

    @FXML
    private Label erroEmail;

    @FXML
    private TextField campoTelefone;

    @FXML
    private Label erroTelefone;

    @FXML
    private TextField campoSenha;

    @FXML
    private Label erroSenha;

    @FXML
    private TextField campoconfirmaSenha;

    @FXML
    private Label erroconfirmaSenha;

    @FXML
    private Label confirmaCadastro;

    private boolean validaEmail(String email) {
        if (email == null) {
            return false;
        }
        for (int i = 0; i < email.length(); i++){
            if (i + 1 < email.length() - 1 && email.charAt(i) == '@' && email.substring(i + 1).equals("gmail.com"))
                return true;      
        }
        return false;
        
    }



    private boolean verificaFormulario(){
        boolean valido = true;

        if (campoNome.getText().trim().isEmpty()){
            erroNome.setVisible(true);
            valido = false;
        }
        else
            erroNome.setVisible(false);

        String email = campoEmail.getText().trim();

        if (email.isEmpty()){
            erroEmail.setText("*Campo obrigatório!");
            erroEmail.setVisible(true); 
            valido = false;
        }
        else if(!validaEmail(email)){
            erroEmail.setText("*Email inválido!");
            erroEmail.setVisible(true);
            valido = false;
        }
        else
            erroEmail.setVisible(false);
        

        if (campoTelefone.getText().trim().length() != 15){
            erroTelefone.setVisible(true);
            valido = false;
        }
        else
            erroTelefone.setVisible(false);

        String senha = campoSenha.getText();
        String confirmaSenha = campoconfirmaSenha.getText();

        if (senha.isEmpty()){
            erroSenha.setText("*Campo obrigatório!");
            erroSenha.setVisible(true);
            valido = false;
        }
        else
            erroSenha.setVisible(false);

        if (!senha.equals(confirmaSenha)){
            erroconfirmaSenha.setVisible(true);
            valido = false;
        }
        else{
            erroconfirmaSenha.setVisible(false);
        }

        return valido;
    }

    

    @FXML
    private void initialize(){
        Platform.runLater(() ->campoNome.requestFocus());

        campoNome.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoEmail.requestFocus(); // Dá o foco (joga o cursor) para o campo de senha
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });

        // 2. Quando apertar Enter no campo de Senha, aí sim dispara o Login
        campoEmail.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
            campoTelefone.requestFocus();
            event.consume();
            }
        });

        campoEmail.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoTelefone.requestFocus(); // Dá o foco (joga o cursor) para o campo de senha
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });

        campoTelefone.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoSenha.requestFocus(); // Dá o foco (joga o cursor) para o campo de senha
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });

        campoSenha.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoconfirmaSenha.requestFocus(); // Dá o foco (joga o cursor) para o campo de senha
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });

        campoconfirmaSenha.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                aoClicarbotaoCadastrar();
                event.consume(); // Avisa o JavaFX que o Enter já foi tratado aqui
            }
        });
    } 

    @FXML
    private void aoClicarbotaoCadastrar(){
        if (!verificaFormulario())
            return;
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String telefone = campoTelefone.getText();
        String senha = campoSenha.getText();
        
        Usuario newUser = new Usuario(nome, email, telefone, senha);
        App.listaUsuarios.add(newUser);

        confirmaCadastro.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));

        pausa.setOnFinished(event ->{
            try {
            // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
            Parent root = loader.load();

            // 2. Pega a janela atual (Stage)
            Stage stage = (Stage) campoSenha.getScene().getWindow();

            // 3. Cria a nova cena (Garante que Scene está com 'C')
            Scene novaCena = new Scene(root);

            // 4. Configura e mostra a nova janela
            stage.setScene(novaCena);
            stage.setTitle("Agenda - Login");
            stage.centerOnScreen();
            stage.show();

            } catch (IOException e) {
                // Se o arquivo teste2.fxml sumir ou estiver com erro, o Java avisa aqui sem travar o programa
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        });
    pausa.play();
    }
   
    
    @FXML
    private void aoClicarBotaoVoltar(){
        try {
        // 1. Carrega o novo arquivo FXML (Protegido dentro do try)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("prelogin.fxml"));
        Parent root = loader.load();

        // 2. Pega a janela atual (Stage)
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();

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

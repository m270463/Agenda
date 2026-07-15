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
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CriacontaController implements Validavel {

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

    @FXML
    private AnchorPane anchor;

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

    @Override
    public boolean validar(){
        boolean valido = true;

        if (campoNome.getText().trim().isEmpty()){
            erroNome.setText("*Campo obrigatório!");
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
        

        if (campoTelefone.getText().replace(" ","").length() != 14){
            erroTelefone.setText("*Telefone inválido!");
            erroTelefone.setVisible(true);
            valido = false;
        }
        else if (campoTelefone.getText().isBlank()){
            erroTelefone.setText("Campo obrigatório!");
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
            erroconfirmaSenha.setText("Senhas incompativeis!");
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

        erroNome.managedProperty().bind(erroNome.visibleProperty());
        erroEmail.managedProperty().bind(erroEmail.visibleProperty());
        erroTelefone.managedProperty().bind(erroTelefone.visibleProperty());
        erroSenha.managedProperty().bind(erroSenha.visibleProperty());
        erroconfirmaSenha.managedProperty().bind(erroconfirmaSenha.visibleProperty());
        confirmaCadastro.managedProperty().bind(confirmaCadastro.visibleProperty());


        Platform.runLater(() ->anchor.requestFocus());
        formataTelefone(campoTelefone);
        campoNome.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoEmail.requestFocus(); 
                event.consume(); 
            }
        });

        campoEmail.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
            campoTelefone.requestFocus();
            event.consume();
            }
        });

        campoEmail.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoTelefone.requestFocus(); 
                event.consume(); 
            }
        });

        campoTelefone.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoSenha.requestFocus(); 
                event.consume(); 
            }
        });

        campoSenha.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                campoconfirmaSenha.requestFocus(); 
                event.consume(); 
            }
        });

        campoconfirmaSenha.setOnKeyPressed((KeyEvent event) -> {
        if (event.getCode() == KeyCode.ENTER) {
                aoClicarbotaoCadastrar();
                event.consume(); 
            }
        });

        
    } 


    @FXML
    private void aoClicarbotaoCadastrar(){
        if (!validar())
            return;
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String telefone = campoTelefone.getText();
        String senha = campoSenha.getText();
        
        Usuario newUser = new Usuario(nome, email, telefone, senha);
        App.listaUsuarios.add(newUser);
        confirmaCadastro.setText("Conta criada!");
        confirmaCadastro.setVisible(true);
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));

        pausa.setOnFinished(event ->{
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) campoSenha.getScene().getWindow();

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
   
    
    @FXML
    private void aoClicarBotaoVoltar(){
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) botaoVoltar.getScene().getWindow();

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

    private void formataTelefone(TextField campoTelefone){
    campoTelefone.setTextFormatter(new TextFormatter<>(change -> {
    if (change.isDeleted() || change.getText().isEmpty()) {
        return change;
    }

    if (!change.getText().matches("[0-9]*")) {
        return null;
    }

    String textoFuturo = change.getControlNewText();

    if (textoFuturo.length() > 15) {
        return null;
    }

    if (textoFuturo.length() == 1) {
        change.setText("(" + change.getText());
        int novaPosicao = change.getCaretPosition() + 1;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    if (textoFuturo.length() == 3) {
        change.setText(change.getText() +  ") ");
        int novaPosicao = change.getCaretPosition() + 2;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    if (textoFuturo.length() == 4) {
        change.setText(") " + change.getText() );
        int novaPosicao = change.getCaretPosition() + 2;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }
    


    if (textoFuturo.length() == 10){
        change.setText(change.getText() +  "-");
        int novaPosicao = change.getCaretPosition() + 1;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    if (textoFuturo.length() == 11) {
        change.setText("-" + change.getText() );
        int novaPosicao = change.getCaretPosition() + 1;
        change.setCaretPosition(novaPosicao);
        change.setAnchor(novaPosicao);
    }

    return change;
    }));
    }





}

package com.agenda;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static ArrayList<Usuario> listaUsuarios = GerenciadorDados.carregarUsuarios();



    @Override
    public void start(Stage stage) {
        try {
            
            // 1. Carrega o arquivo FXML da tela de login
            // ATENÇÃO: Mude "teste.fxml" para o nome exato do seu arquivo se for diferente!
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("prelogin.fxml"));
            
            // 2. Cria a cena com o FXML carregado (Largura: 600, Altura: 400)
            // Você pode ajustar esses números para o tamanho que preferir
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            
            stage.setOnCloseRequest(event->{
                GerenciadorDados.salvarUsuarios(listaUsuarios);
            });

            // 3. Configura a janela do sistema
            stage.setTitle("Agenda - Início");
            stage.setScene(scene);
            stage.setResizable(true); // Impede o usuário de maximizar e quebrar o layout
            stage.show(); // Mostra a tela
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique se o nome está correto!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Método nativo do JavaFX que inicializa todo o sistema e chama o método 'start'
        launch();
    }
}
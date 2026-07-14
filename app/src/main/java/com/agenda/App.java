package com.agenda;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Persistivel<Usuario> gerenciadorDados = new GerenciadorDados();
    public static ArrayList<Usuario> listaUsuarios = gerenciadorDados.carregar();
    public static Usuario usuarioaAtivo;

    @Override
    public void start(Stage stage) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teste.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gerenciadorDados.salvar(listaUsuarios);
        }));

            stage.setTitle("Agenda - Início");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique se o nome está correto!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
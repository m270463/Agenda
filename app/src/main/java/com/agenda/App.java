package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação Agenda.
 * <p>
 * Esta classe é responsável por inicializar a interface gráfica construída em JavaFX,
 * gerenciar o ciclo de vida do aplicativo e lidar com a persistência de dados dos usuários
 * ao iniciar e encerrar o programa.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class App extends Application {

    /**
     * Gerenciador responsável pela persistência de dados dos usuários.
     * Utiliza a interface {@link Persistivel} para salvar e carregar as informações.
     */
    public static Persistivel<Usuario> gerenciadorDados = new GerenciadorDados();

    /**
     * Lista global que armazena todos os usuários cadastrados no sistema.
     * É inicializada carregando os dados previamente salvos pelo {@link #gerenciadorDados}.
     */
    public static ArrayList<Usuario> listaUsuarios = gerenciadorDados.carregar();

    /**
     * Armazena o usuário que está atualmente autenticado e ativo na sessão do aplicativo.
     */
    public static Usuario usuarioaAtivo;


    private LocalDate ultimaVarredura = LocalDate.now().minusDays(2);

    /**
     * Inicializa o palco (Stage) principal da aplicação JavaFX.
     * <p>
     * Este método carrega o arquivo FXML inicial, define a cena, configura um 
     * <i>shutdown hook</i> para garantir que os dados sejam salvos automaticamente 
     * quando a aplicação for fechada e exibe a janela.
     * </p>
     *
     * @param stage O palco principal (primary stage) fornecido pelo JavaFX para esta aplicação.
     */
    @Override
    public void start(Stage stage) {
        try {
            Notificador notificar = new Notificador();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teste.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setOnCloseRequest(event -> {
                notificar.fechar(); 
                
                javafx.application.Platform.exit(); 
                
                System.exit(0); 
            });

            stage.setTitle("Agenda - Início");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
            if (!ultimaVarredura.equals(LocalDate.now())){
                notificar.iniciarVerificacaoDiaria();
                ultimaVarredura = LocalDate.now();
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique se o nome está correto!");
            e.printStackTrace();
        }
    }

    /**
     * Método de entrada padrão do Java.
     * <p>
     * Dispara o ciclo de vida do JavaFX chamando internamente o método {@code launch()}.
     * </p>
     *
     * @param args Argumentos de linha de comando (não utilizados nesta aplicação).
     */
    public static void main(String[] args) {
        launch();
    }
}
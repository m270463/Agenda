package com.agenda;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {
    public static final Dotenv dotenv = Dotenv.load();     
    public static Persistivel<Usuario> gerenciadorDados = new GerenciadorDados();
    public static ArrayList<Usuario> listaUsuarios = gerenciadorDados.carregar();
    public static Usuario usuarioaAtivo;

    private LocalDate ultimaVarredura = gerenciadorDados.carregarUltimaVerificacao();

    @Override
    public void start(Stage stage) {
        try {
            Platform.setImplicitExit(false);

            Notificador notificar = new Notificador();
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("teste.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setOnCloseRequest(event -> {
                event.consume(); 
                stage.hide();    
            });

            stage.setTitle("Agenda - Início");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

            configurarSystemTray(stage, notificar);

                notificar.iniciarVerificacaoDiaria();
            ultimaVarredura = LocalDate.now();
            

        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo FXML. Verifique se o nome está correto!");
            e.printStackTrace();
        }
    }

  private void configurarSystemTray(Stage stage, Notificador notificar) {
    if (!SystemTray.isSupported()) {
        System.out.println("SystemTray não é suportado neste sistema.");
        return;
    }

    try {
        Toolkit.getDefaultToolkit();
        SystemTray tray = SystemTray.getSystemTray();

        java.awt.Image image = null;
        URL imgUrl = App.class.getResource("/imagens_agenda/Captura de tela de 2026-08-04 17-34-30.png");

        if (imgUrl != null) {
            BufferedImage originalImage = ImageIO.read(imgUrl);
            
            image = originalImage.getScaledInstance(22, 22, java.awt.Image.SCALE_SMOOTH);
        } else {
            image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        }

        PopupMenu popup = new PopupMenu();

        java.awt.MenuItem itemAbrir = new java.awt.MenuItem("Abrir Agenda");
        itemAbrir.addActionListener(e -> Platform.runLater(() -> {
            stage.show();
            stage.toFront();
        }));

        java.awt.MenuItem itemSair = new java.awt.MenuItem("Sair Definitivamente");
        itemSair.addActionListener(e -> {
            notificar.fechar();
            Platform.exit();
            System.exit(0);
        });

        popup.add(itemAbrir);
        popup.addSeparator();
        popup.add(itemSair);

        TrayIcon trayIcon = new TrayIcon(image, "Agenda", popup);
        
        trayIcon.setImageAutoSize(true);

        trayIcon.addActionListener(e -> Platform.runLater(() -> {
            stage.show();
            stage.toFront();
        }));

        tray.add(trayIcon);

    } catch (Exception e) {
        System.err.println("Erro ao inicializar o SystemTray: " + e.getMessage());
    }
}

    public static void main(String[] args) {
        launch();
    }
}
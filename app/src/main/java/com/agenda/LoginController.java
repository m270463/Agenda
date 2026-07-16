package com.agenda;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller responsável pela tela de autenticação e login de usuários na aplicação de Agenda.
 * <p>
 * Implementa a interface {@link Validavel}. Esta classe gerencia a validação das credenciais 
 * de acesso (e-mail e senha) contra a lista de usuários carregados no sistema, define o usuário 
 * ativo na sessão atual e lida com o redirecionamento de telas para o calendário principal ou 
 * para o fluxo de criação de novas contas.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class LoginController implements Validavel {

    /** Campo de texto destinado à inserção do e-mail do usuário (utilizado como identificador de login). */
    @FXML
    private TextField campoUsuario;

    /** Campo de texto protegido por máscara destinado à inserção da senha do usuário. */
    @FXML
    private PasswordField campoSenha;

    /** Label que exibe mensagens de erro em caso de falha de autenticação (ex: credenciais incorretas). */
    @FXML
    private Label textoErro;

    /** Painel raiz da interface gráfica, utilizado para desviar o foco inicial automático dos campos de texto. */
    @FXML
    private AnchorPane anchor;

    /**
     * Inicializa a tela de login logo após o carregamento do arquivo FXML.
     * <p>
     * Requisita o foco do painel principal de forma assíncrona para evitar que o cursor comece 
     * focado no campo de usuário antes da interação. Configura também os atalhos de teclado 
     * para navegação fluida: pressionar a tecla <b>Enter</b> no campo de usuário transfere o foco para a senha, 
     * e no campo de senha dispara automaticamente a tentativa de login.
     * </p>
     */
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

    /**
     * Valida as credenciais inseridas pelo usuário na tela de login.
     * <p>
     * Percorre a lista estática global de cadastros ({@code App.listaUsuarios}) procurando uma correspondência 
     * exata de e-mail e senha. Se a combinação for encontrada, define o usuário ativo da sessão atual 
     * ({@code App.usuarioaAtivo}) com a instância do usuário autenticado.
     * </p>
     *
     * @return {@code true} se as credenciais de login existirem e estiverem corretas; 
     * {@code false} caso contrário.
     */
    @Override
    public boolean validar() {
        for (int i = 0; i < App.listaUsuarios.size(); i++) {
            if (App.listaUsuarios.get(i).getEmail().equals(campoUsuario.getText()) && 
                App.listaUsuarios.get(i).getSenha().equals(campoSenha.getText())) {
                
                App.usuarioaAtivo = App.listaUsuarios.get(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Dispara o fluxo de autenticação ao clicar no botão de Login ou pressionar Enter no campo de senha.
     * <p>
     * Se o método {@link #validar()} retornar {@code true}, o usuário é redirecionado para a visualização 
     * principal do calendário através da carga do arquivo {@code calendario.fxml}. Caso contrário, 
     * a label de erro ({@code textoErro}) é tornada visível na tela.
     * </p>
     */
    @FXML
    private void aoClicarBotaoLogin() {
        if (validar()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoUsuario.getScene().getWindow();
                stage.getScene().setRoot(root);
                root.applyCss();
                root.layout();
                stage.setTitle("Agenda - Principal");

            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        } else {
            textoErro.setVisible(true);
        }
    }

    /**
     * Direciona o fluxo da aplicação para a tela de registro de novos usuários.
     * <p>
     * Realiza a troca da cena ativa na janela principal para exibir a interface declarada 
     * no arquivo {@code criarconta.fxml}.
     * </p>
     */
    @FXML
    private void aoClicarBotaoCriarConta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("criarconta.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) campoUsuario.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Início");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }
}
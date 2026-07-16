package com.agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/**
 * Classe de testes visuais automatizados usando TestFX.
 * Simula a interação real de um usuário digitando e clicando na tela.
 */
@ExtendWith(ApplicationExtension.class)
public class TesteControllersUI {

    /**
     * Inicializa o ambiente JavaFX carregando a tela de Login.
     * Utiliza a referência da classe App para localizar o arquivo FXML sem erros de path.
     */
    @Start
    public void start(Stage stage) throws IOException {
        // Inicializa a lista global com um usuário de teste
        App.listaUsuarios = new ArrayList<>();
        Usuario usuarioTeste = new Usuario("Testador", "teste@agenda.com", "123456789", "123");
        App.listaUsuarios.add(usuarioTeste);
        App.usuarioaAtivo = null;

        // Carrega o FXML a partir do pacote do App
        FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Agenda - Login");
        stage.show();
    }

    @Test
    @DisplayName("Login: Deve exibir mensagem de erro ao tentar logar com dados inválidos")
    public void deveMostrarErroLoginInvalido(FxRobot robot) {
        // 1. Simula o clique e a digitação nos campos de texto
        // Certifique-se de que no seu login.fxml o fx:id do campo de e-mail é "campoUsuario" (ou "campoEmail")
        robot.clickOn("#campoUsuario").write("usuario_errado@agenda.com");
        robot.clickOn("#campoSenha").write("senha_incorreta");

        // 2. Clica no botão "Entrar" (Busca pelo texto visível no botão)
        robot.clickOn("Entrar");

        // 3. Valida se a Label de erro ficou visível
        // Certifique-se de que a sua Label de erro no FXML possui o fx:id "textoErro"
        Label textoErro = robot.lookup("#textoErro").queryAs(Label.class);
        
        assertTrue(textoErro.isVisible(), "A label de erro deveria estar visível na tela.");
        assertFalse(textoErro.getText().isEmpty(), "A mensagem de erro não deveria estar vazia.");
    }

    @Test
    @DisplayName("Login: Deve autenticar o usuário ativo com credenciais válidas")
    public void deveLogarComSucesso(FxRobot robot) {
        // 1. Digita as credenciais corretas cadastradas no método start()
        robot.clickOn("#campoUsuario").write("teste@agenda.com");
        robot.clickOn("#campoSenha").write("123");

        // 2. Simula o pressionar da tecla ENTER no campo de senha para efetuar o login
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // 3. Valida se a lógica do controller definiu o usuário ativo global do sistema
        assertNotNull(App.usuarioaAtivo, "O login falhou. O usuário ativo não foi definido.");
        assertEquals("teste@agenda.com", App.usuarioaAtivo.getEmail());
    }

    @Test
    @DisplayName("Navegação: Deve abrir a tela de Criar Conta ao clicar no botão correspondente")
    public void deveNavegarParaCriarConta(FxRobot robot) {
        // 1. Clica no botão "Criar Conta" na tela de login
        robot.clickOn("Criar Conta");

        // 2. Valida se a nova janela foi carregada verificando se o botão "Cadastrar" existe na nova cena
        // Substitua "Cadastrar" pelo texto exato do botão de envio da sua tela de criar conta
        assertNotNull(robot.lookup("Cadastrar").queryButton(), "Não foi possível confirmar a transição para a tela de cadastro.");
    }
}
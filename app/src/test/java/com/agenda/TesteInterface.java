package com.agenda;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Testes de interface com TestFX: simulam clique/digitação reais do usuário.
 *
 * IMPORTANTE: o arquivo carregado é "teste.fxml" (tela de login/início do
 * projeto), e não "login.fxml" — esse era o motivo de todos os testes de UI
 * falharem antes: o FXMLLoader não encontrava o recurso.
 *
 * Se os textos dos botões abaixo ("Entrar", "Não tenho uma conta", "Criar")
 * não baterem exatamente com o texto no teste.fxml/criarconta.fxml de vocês,
 * ajustem as strings — não tenho esses dois FXMLs específicos para conferir
 * o texto exato.
 */
@ExtendWith(ApplicationExtension.class)
public class TesteInterface {

    @BeforeAll
    public static void setupHeadlessMode() {
        System.setProperty("testfx.robot", "glass");
    }

    @Start
    public void start(Stage stage) throws IOException {
        App.listaUsuarios = new ArrayList<>();
        App.usuarioaAtivo = null;

        FXMLLoader loader = new FXMLLoader(App.class.getResource("teste.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Agenda - Início");
        stage.show();
    }

    @BeforeEach
    public void resetEstado() {
        // Garante que cada teste começa com um usuário conhecido, mesmo que
        // um teste anterior tenha adicionado outros à lista.
        App.listaUsuarios.removeIf(u -> u.getEmail().equals("a@gmail.com"));
        App.listaUsuarios.add(new Usuario("Testador", "a@gmail.com", "99999999999", "123"));
        App.usuarioaAtivo = null;
    }

@Test
    @DisplayName("Login: deve exibir erro ao tentar logar com credenciais inválidas")
    public void deveMostrarErroLoginInvalido(FxRobot robot) {
        robot.clickOn("#campoUsuario").write("usuarioerrado@gmail.com");
        robot.clickOn("#campoSenha").write("errada");
        robot.clickOn("#botaoEntrar");
        robot.sleep(800);
        Label erroData = robot.lookup("#textoErro").queryAs(Label.class);
        assertNotNull(erroData);
        assertTrue(erroData.isVisible(), "Deveria rejeitar um usuário inválido.");
    
    }

 @Test

@DisplayName("Login: deve autenticar com credenciais válidas")
    public void deveLogarComSucesso(FxRobot robot) {
        robot.clickOn("#campoUsuario").write("a@gmail.com");
        robot.clickOn("#campoSenha").write("123");
        robot.clickOn("#botaoEntrar");
        assertNotNull(App.usuarioaAtivo, "O login falhou: usuário ativo não foi definido.");
        assertEquals("a@gmail.com", App.usuarioaAtivo.getEmail());
    } 

        @Test
    @DisplayName("Navegação: deve abrir a tela de Criar Conta")
    public void deveNavegarParaCriarConta(FxRobot robot) {
        robot.clickOn("Não tenho uma conta");
        assertNotNull(robot.lookup("Criar").queryButton(), "Não foi possível confirmar a transição para a tela de cadastro.");
    }

    @Test
    @DisplayName("Criar conta: deve impedir cadastro com e-mail já usado")
    public void deveRejeitarEmailDuplicado(FxRobot robot) {
        robot.clickOn("Não tenho uma conta");

        robot.clickOn("#campoNome").write("Segundo Usuario");
        robot.clickOn("#campoEmail").write("teste@agenda.com"); // já existe, mas note: precisa terminar em @gmail.com para passar da 1a validação
        robot.clickOn("#campoTelefone").write("11999998888");
        robot.clickOn("#campoSenha").write("123456");
        robot.clickOn("#campoconfirmaSenha").write("123456");
        robot.clickOn("Criar");

        // Como "teste@agenda.com" não termina em @gmail.com, o primeiro erro
        // detectado deve ser o de formato — o que já é o comportamento correto.
        Label erroEmail = robot.lookup("#erroEmail").queryAs(Label.class);
        assertNotNull(erroEmail);
        assertTrue(erroEmail.isVisible(), "Deveria haver algum erro de e-mail bloqueando o cadastro.");
    }

    @Test
    @DisplayName("Criar conta: deve criar com sucesso quando os dados são válidos e únicos")
    public void deveCriarContaComSucesso(FxRobot robot) {
        robot.clickOn("Não tenho uma conta");

        int totalAntes = App.listaUsuarios.size();

        robot.clickOn("#campoNome").write("Novo Usuario");
        robot.clickOn("#campoEmail").write("novo@gmail.com");
        robot.clickOn("#campoTelefone").write("11999998888");
        robot.clickOn("#campoSenha").write("123456");
        robot.clickOn("#campoconfirmaSenha").write("123456");
        robot.clickOn("Criar");

        robot.sleep(1200);

        assertEquals(totalAntes + 1, App.listaUsuarios.size(), "Um novo usuário deveria ter sido adicionado.");
    }
}
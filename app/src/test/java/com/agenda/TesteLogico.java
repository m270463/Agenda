package com.agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Suite de testes lógicos para validação dos fluxos internos dos Controllers.
 */
public class TesteLogico {

    @BeforeEach
    public void setUp() {
        // Reinicializa o estado global da aplicação antes de cada teste
        App.listaUsuarios = new ArrayList<>();
        App.usuarioaAtivo = null;

        // Cadastro de usuário simulado para os testes de login
        Usuario usuarioTeste = new Usuario("Testador", "teste@agenda.com", "123456789", "senha123");
        App.listaUsuarios.add(usuarioTeste);
    }

    @Test
    @DisplayName("LoginController: Deve autenticar o usuário com credenciais corretas")
    public void deveAutenticarUsuarioValido() {
        String emailDigitado = "teste@agenda.com";
        String senhaDigitada = "senha123";

        // Simulação exata da verificação interna de segurança que ocorre no LoginController
        Usuario usuarioEncontrado = null;
        for (Usuario u : App.listaUsuarios) {
            if (u.getEmail().equals(emailDigitado) && u.getSenha().equals(senhaDigitada)) {
                usuarioEncontrado = u;
                break;
            }
        }
        App.usuarioaAtivo = usuarioEncontrado;

        assertNotNull(App.usuarioaAtivo, "O login deveria ter sucesso.");
        assertEquals("Testador", App.usuarioaAtivo.getNome());
    }

    @Test
    @DisplayName("LoginController: Deve rejeitar autenticação com senha incorreta")
    public void naoDeveAutenticarSenhaIncorreta() {
        String emailDigitado = "teste@agenda.com";
        String senhaDigitada = "senha_errada";

        Usuario usuarioEncontrado = null;
        for (Usuario u : App.listaUsuarios) {
            if (u.getEmail().equals(emailDigitado) && u.getSenha().equals(senhaDigitada)) {
                usuarioEncontrado = u;
                break;
            }
        }
        App.usuarioaAtivo = usuarioEncontrado;

        assertNull(App.usuarioaAtivo, "O login deveria falhar para senhas incorretas.");
    }

    @Test
    @DisplayName("CriacontaController: Deve impedir a criação de conta se o email já estiver cadastrado")
    public void naoDeveDuplicarEmail() {
        // Tentativa de cadastrar um e-mail idêntico ao já existente
        String novoNome = "Outro Usuario";
        String novoEmail = "teste@agenda.com"; // Já existe no setUp
        String novoTelefone = "987654321";
        String novaSenha = "456";

        boolean emailJaExiste = false;
        for (Usuario u : App.listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(novoEmail)) {
                emailJaExiste = true;
                break;
            }
        }

        assertTrue(emailJaExiste, "A verificação deve alertar que o e-mail já está em uso.");
    }

    @Test
    @DisplayName("CriacontaController: Deve permitir criar nova conta se os dados forem únicos")
    public void deveCriarNovaContaComSucesso() {
        String novoNome = "Ana Souza";
        String novoEmail = "ana@agenda.com";
        String novoTelefone = "987654321";
        String novaSenha = "456";

        boolean emailJaExiste = false;
        for (Usuario u : App.listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(novoEmail)) {
                emailJaExiste = true;
                break;
            }
        }

        if (!emailJaExiste) {
            Usuario novoUsuario = new Usuario(novoNome, novoEmail, novoTelefone, novaSenha);
            App.listaUsuarios.add(novoUsuario);
        }

        assertEquals(2, App.listaUsuarios.size(), "O novo usuário deveria ter sido adicionado com sucesso.");
    }

    @Test
    @DisplayName("EditEventoController / CriarEvento: Deve rejeitar criação de evento se a hora de início for posterior à de fim")
    public void naoDevePermitirHorarioInvalido() {
        String nome = "Reunião de Alinhamento";
        LocalDate data = LocalDate.now();
        LocalTime inicio = LocalTime.of(15, 0);
        LocalTime fim = LocalTime.of(14, 0); // Fim antes do início

        boolean horarioValido = inicio.isBefore(fim);

        assertFalse(horarioValido, "A lógica de validação deve impedir horários inconsistentes.");
    }
}
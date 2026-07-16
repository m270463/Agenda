package com.agenda;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Testes de fluxo completo: login -> calendário -> criar evento.
 * Cobre calendarioController, controllerEventos e CriaeventoController.
 */
@ExtendWith(ApplicationExtension.class)
public class TesteFluxoEventos {

    private Stage stage;

    @Start
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        App.listaUsuarios = new ArrayList<>();
        App.usuarioaAtivo = new Usuario("Testador", "teste@agenda.com", "123456789", "123");
        App.listaUsuarios.add(App.usuarioaAtivo);

        carregarCalendario();
    }

    private void carregarCalendario() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("calendario.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Agenda - Calendário");
        stage.show();
    }

    @BeforeEach
    public void resetTela() throws Exception {
        App.usuarioaAtivo.getAgenda().clear();
        App.usuarioaAtivo.getAgendaRepetitiva().clear();

        // Garante que TODO teste começa a partir da tela do calendário,
        // independente de qual tela o teste anterior deixou aberta.
        Platform.runLater(() -> {
            try {
                carregarCalendario();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @DisplayName("Calendário: deve navegar entre meses sem erros")
    public void deveNavegarEntreMeses(FxRobot robot) {
        robot.clickOn("#avancar");
        robot.sleep(200);
        robot.clickOn("#avancar");
        robot.sleep(200);
        robot.clickOn("#voltar");
        robot.sleep(200);
        robot.clickOn("#voltar");
        robot.sleep(200);

        assertNotNull(robot.lookup("#calendario").query());
    }

    @Test
    @DisplayName("Criar evento: deve bloquear submissão com campos obrigatórios vazios")
    public void deveBloquearCriacaoComCamposVazios(FxRobot robot) {
        robot.clickOn("#botaoAdicionar");
        robot.sleep(200);

        robot.clickOn("Criar");

        Label erroTitulo = robot.lookup("#erroTitulo").queryAs(Label.class);
        assertNotNull(erroTitulo);
        assertTrue(erroTitulo.isVisible(), "Deveria bloquear com o título vazio.");
    }

    @Test
    @DisplayName("Criar evento: deve rejeitar data em formato inválido")
    public void deveRejeitarDataInvalida(FxRobot robot) {
        robot.clickOn("#botaoAdicionar");
        robot.sleep(200);

        robot.clickOn("#titulo").write("Reunião de Teste");
        robot.clickOn("#diaInicio").write("31/02/2026");
        robot.clickOn("Criar");

        Label erroData = robot.lookup("#erroDiaInicio").queryAs(Label.class);
        assertNotNull(erroData);
        assertTrue(erroData.isVisible(), "Deveria rejeitar 31/02, que não existe.");
    }

    @Test
    @DisplayName("Criar evento: deve rejeitar horário de fim anterior ao de início")
    public void deveRejeitarHorarioInvalido(FxRobot robot) {
        robot.clickOn("#botaoAdicionar");
        robot.sleep(200);

        robot.clickOn("#titulo").write("Reunião de Teste");
        robot.clickOn("#diaInicio").write("20/07/2026");
        robot.clickOn("#horaInicio").write("1500");
        robot.clickOn("#horaFim").write("1400");
        robot.clickOn("#Comborepeticao").clickOn("Nunca");
        robot.clickOn("Criar");

        Label erroHoraFim = robot.lookup("#erroHoraFim").queryAs(Label.class);
        assertNotNull(erroHoraFim);
        assertTrue(erroHoraFim.isVisible(), "Deveria rejeitar horário de fim antes do início.");
    }

    @Test
    @DisplayName("Criar evento: 'Dia inteiro' deve esconder os campos de horário")
    public void deveEsconderHorarioComDiaInteiro(FxRobot robot) {
        robot.clickOn("#botaoAdicionar");
        robot.sleep(200);

        var toggle = (org.controlsfx.control.ToggleSwitch) robot.lookup("#btnInterruptor").query();
        assertFalse(toggle.isSelected());

        robot.clickOn("#btnInterruptor");
        robot.sleep(200);

        var horaInicio = robot.lookup("#horaInicio").query();
        assertFalse(horaInicio.isVisible(), "O campo de hora de início deveria sumir com 'Dia inteiro' ativado.");
    }
    
    @Test
    @DisplayName("Criar evento: deve criar com sucesso e adicionar na agenda do usuário")
    public void deveCriarEventoComSucesso(FxRobot robot) {
        robot.clickOn("#botaoAdicionar");
        robot.sleep(200);

        robot.clickOn("#titulo").write("Reunião de Alinhamento");
        robot.clickOn("#diaInicio").write("20/07/2026");
        robot.clickOn("#horaInicio").write("1400");
        robot.clickOn("#horaFim").write("1500");
        robot.clickOn("#descricao").write("Pauta X");
        robot.clickOn("#Comborepeticao").clickOn("Nunca");
        robot.clickOn("Criar");

        robot.sleep(1200);

        var agendaDoDia = App.usuarioaAtivo.getAgenda().get(java.time.LocalDate.of(2026, 7, 20));
        assertNotNull(agendaDoDia, "O evento deveria ter sido adicionado na agenda do dia 20/07/2026.");
        assertEquals(1, agendaDoDia.size());
        assertEquals("Reunião de Alinhamento", agendaDoDia.get(0).getNome());
    }
}
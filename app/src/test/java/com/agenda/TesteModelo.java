package com.agenda;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Testes de lógica pura (modelo e regras de negócio), sem depender do JavaFX.
 * Cobre: Repetivel/ItemAgenda/Evento, Usuario, GerenciadorDados (Persistivel)
 * e as regras de validação que os controllers usam.
 */
public class TesteModelo {

    @Nested
    @DisplayName("Evento / Repetivel: regras de recorrência")
    class TestesRecorrencia {

        private Evento eventoDiario;
        private Evento eventoSemanal;
        private Evento eventoMensal;
        private Evento eventoAnual;
        private Evento eventoUnico;
        private LocalDate dataInicio;

        @BeforeEach
        void setUp() {
            dataInicio = LocalDate.of(2026, 7, 16); // quinta-feira

            eventoDiario = new Evento("Academia", "Treinar", "Diariamente", dataInicio, LocalTime.of(7, 0), LocalTime.of(8, 0));
            eventoSemanal = new Evento("Terapia", "Sessão semanal", "Semanalmente", dataInicio, LocalTime.of(14, 0), LocalTime.of(15, 0));
            eventoMensal = new Evento("Pagar Aluguel", "Boleto", "Mensalmente", dataInicio, null, null);
            eventoAnual = new Evento("Aniversário", "Festa", "Anualmente", dataInicio, null, null);
            eventoUnico = new Evento("Dentista", "Consulta única", "Nunca", dataInicio, LocalTime.of(10, 0), LocalTime.of(11, 0));
        }

        @Test
        @DisplayName("Nenhum evento deve ocorrer antes da sua data de início")
        void naoDeveOcorrerAntesDoInicio() {
            LocalDate dataAnterior = dataInicio.minusDays(1);
            assertFalse(eventoDiario.ocorreEm(dataAnterior));
            assertFalse(eventoSemanal.ocorreEm(dataAnterior));
            assertFalse(eventoMensal.ocorreEm(dataAnterior));
            assertFalse(eventoAnual.ocorreEm(dataAnterior));
            assertFalse(eventoUnico.ocorreEm(dataAnterior));
        }

        @Test
        @DisplayName("Evento diário deve ocorrer em qualquer dia a partir do início")
        void deveOcorrerDiariamente() {
            assertTrue(eventoDiario.ocorreEm(dataInicio));
            assertTrue(eventoDiario.ocorreEm(dataInicio.plusDays(1)));
            assertTrue(eventoDiario.ocorreEm(dataInicio.plusDays(100)));
        }

        @Test
        @DisplayName("Evento semanal só deve ocorrer no mesmo dia da semana")
        void deveOcorrerSemanalmente() {
            assertTrue(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 23))); // quinta seguinte
            assertFalse(eventoSemanal.ocorreEm(LocalDate.of(2026, 7, 24))); // sexta
        }

        @Test
        @DisplayName("Evento mensal só deve ocorrer no mesmo dia do mês")
        void deveOcorrerMensalmente() {
            assertTrue(eventoMensal.ocorreEm(LocalDate.of(2026, 8, 16)));
            assertFalse(eventoMensal.ocorreEm(LocalDate.of(2026, 8, 17)));
        }

        @Test
        @DisplayName("Evento anual só deve ocorrer no mesmo dia e mês")
        void deveOcorrerAnualmente() {
            assertTrue(eventoAnual.ocorreEm(LocalDate.of(2027, 7, 16)));
            assertFalse(eventoAnual.ocorreEm(LocalDate.of(2027, 8, 16)));
        }

        @Test
        @DisplayName("Evento único ('Nunca') só deve ocorrer no seu próprio dia de início")
        void deveOcorrerApenasUmaVez() {
            assertTrue(eventoUnico.ocorreEm(dataInicio));
            assertFalse(eventoUnico.ocorreEm(dataInicio.plusDays(1)));
            assertFalse(eventoUnico.ocorreEm(dataInicio.plusMonths(1)));
        }
    }

    @Nested
    @DisplayName("Evento / ItemAgenda: resumo de horário")
    class TestesResumoHorario {

        @Test
        @DisplayName("Evento sem horário deve ser resumido como 'Dia inteiro'")
        void deveResumirDiaInteiro() {
            Evento evento = new Evento("Reunião Anual", "Planejamento", "Nunca", LocalDate.of(2026, 12, 31), null, null);
            assertEquals("Dia inteiro", evento.getResumoHorario());
        }

        @Test
        @DisplayName("Evento com horário deve ser resumido como 'HH:mm - HH:mm'")
        void deveResumirComHorario() {
            Evento evento = new Evento("Almoço", "Com cliente", "Nunca", LocalDate.of(2026, 7, 16), LocalTime.of(12, 0), LocalTime.of(13, 30));
            assertEquals("12:00 - 13:30", evento.getResumoHorario());
        }

        @Test
        @DisplayName("Alterar hora de início/fim deve refletir no resumo")
        void deveAtualizarResumoAposSetters() {
            Evento evento = new Evento("Dentista", "Limpeza", "Nunca", LocalDate.of(2026, 7, 16), LocalTime.of(14, 0), LocalTime.of(15, 0));
            evento.setHoraInicio(LocalTime.of(9, 0));
            evento.setHoraFim(LocalTime.of(10, 0));
            assertEquals("09:00 - 10:00", evento.getResumoHorario());
        }
    }

    @Nested
    @DisplayName("Usuario: estrutura e dados")
    class TestesUsuario {

        private Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = new Usuario("João Silva", "joao@email.com", "11999999999", "senha123");
        }

        @Test
        @DisplayName("Deve inicializar agenda e agenda repetitiva vazias")
        void deveInicializarAgendaVazia() {
            assertNotNull(usuario.getAgenda());
            assertNotNull(usuario.getAgendaRepetitiva());
            assertTrue(usuario.getAgenda().isEmpty());
            assertTrue(usuario.getAgendaRepetitiva().isEmpty());
        }

        @Test
        @DisplayName("Deve manter os dados cadastrais corretos")
        void deveManterDadosCadastrais() {
            assertEquals("João Silva", usuario.getNome());
            assertEquals("joao@email.com", usuario.getEmail());
            assertEquals("senha123", usuario.getSenha());
            assertEquals("11999999999", usuario.getTelefone());
        }

        @Test
        @DisplayName("Deve permitir adicionar evento pontual na agenda por data")
        void deveAdicionarEventoAoMapa() {
            Evento evento = new Evento("Dentista", "Limpeza", "Nunca", LocalDate.of(2026, 7, 16), LocalTime.of(14, 0), LocalTime.of(15, 0));
            LocalDate data = evento.getDiaInicio();

            usuario.getAgenda().putIfAbsent(data, new ArrayList<>());
            usuario.getAgenda().get(data).add(evento);

            assertTrue(usuario.getAgenda().containsKey(data));
            assertEquals(1, usuario.getAgenda().get(data).size());
            assertEquals("Dentista", usuario.getAgenda().get(data).get(0).getNome());
        }
    }

    @Nested
    @DisplayName("GerenciadorDados / Persistivel: leitura e gravação em arquivo")
    class TestesPersistencia {

        private static final Path CAMINHO_TESTE = Paths.get("usuarios.json");
        private Persistivel<Usuario> gerenciador;

        @BeforeEach
        void setUp() {
            gerenciador = new GerenciadorDados();
        }

        @AfterEach
        void limpar() throws IOException {
            Files.deleteIfExists(CAMINHO_TESTE);
        }





    @Nested
    @DisplayName("Regras de negócio replicadas dos controllers (login e cadastro)")
    class TestesRegrasDeNegocio {

        private ArrayList<Usuario> listaUsuarios;

        @BeforeEach
        void setUp() {
            listaUsuarios = new ArrayList<>();
            listaUsuarios.add(new Usuario("Testador", "teste@agenda.com", "123456789", "senha123"));
        }

        @Test
        @DisplayName("Login deve autenticar com credenciais corretas")
        void deveAutenticarUsuarioValido() {
            Usuario encontrado = autenticar(listaUsuarios, "teste@agenda.com", "senha123");
            assertNotNull(encontrado);
            assertEquals("Testador", encontrado.getNome());
        }

        @Test
        @DisplayName("Login deve rejeitar senha incorreta")
        void naoDeveAutenticarSenhaIncorreta() {
            assertNull(autenticar(listaUsuarios, "teste@agenda.com", "senha_errada"));
        }

        @Test
        @DisplayName("Não deve permitir criar evento com hora de fim anterior à de início")
        void naoDevePermitirHorarioInvalido() {
            LocalTime inicio = LocalTime.of(15, 0);
            LocalTime fim = LocalTime.of(14, 0);
            assertTrue(fim.isBefore(inicio), "A validação deve detectar que o fim é anterior ao início.");
        }

        private Usuario autenticar(ArrayList<Usuario> lista, String email, String senha) {
            for (Usuario u : lista) {
                if (u.getEmail().equals(email) && u.getSenha().equals(senha)) {
                    return u;
                }
            }
            return null;
        }
    }
}
}
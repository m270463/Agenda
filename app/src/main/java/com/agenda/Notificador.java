package com.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Notificador {
    private final ScheduledExecutorService agendador = Executors.newSingleThreadScheduledExecutor();

    public void iniciarVerificacaoDiaria() {
        Runnable tarefa = () -> {
            try {
                LocalDate hoje = LocalDate.now();
                LocalDate ultimaVarredura = App.gerenciadorDados.carregarUltimaVerificacao(); 

                if ((ultimaVarredura == null || !ultimaVarredura.equals(hoje)) && App.usuarioaAtivo != null) {
                    
                    Usuario u = App.usuarioaAtivo; 
                    ArrayList<Evento> eventosDia = new ArrayList<>(); 

                    if (u.getAgenda() != null && u.getAgenda().get(hoje.plusDays(1)) != null) {
                        eventosDia.addAll(u.getAgenda().get(hoje.plusDays(1))); 
                    }
                    if (u.getAgendaRepetitiva() != null) { 
                        for (Evento e : u.getAgendaRepetitiva()) { 
                            if (e.ocorreEm(hoje.plusDays(1))) { 
                                eventosDia.add(e); 
                            }
                        }
                    }

                    if (!eventosDia.isEmpty()) {
                        enviarNotificacao(eventosDia, u, hoje.plusDays(1)); 
                    }

                    App.gerenciadorDados.inserirUltimaVerificacao(hoje); 
                }
            } catch (Exception e) {
                System.err.println("❌ Erro no agendador de notificações: " + e.getMessage());
                e.printStackTrace();
            }
        };

        
        agendador.scheduleAtFixedRate(tarefa, 0, 1, TimeUnit.HOURS); 
    }

    private void enviarNotificacao(ArrayList<Evento> eventos, Usuario usuario, LocalDate data) {
        StringBuilder mensagem = new StringBuilder("Olá " + usuario.getNome() + ", você tem " + eventos.size() + 
                          " evento(s) programado(s) para " + data.toString() + ":\n"); 

        for (Evento e : eventos) { 
            if (e.getHoraInicio() != null) 
                mensagem.append("- ").append(e.getNome()).append(" (das ").append(e.getHoraInicio()).append(" às ").append(e.getHoraFim()).append(")\n"); //[cite: 1]
            else
                mensagem.append("- ").append(e.getNome()).append(" (durando o dia inteiro)\n"); 
        }

        System.out.println("📧 Disparando e-mail para: " + usuario.getEmail()); 
        ServicoEmail.enviarAlerta(usuario.getEmail(), "Lembrete de Eventos", mensagem.toString()); 
    }

    public void fechar() {
        agendador.shutdown(); 
    }
}
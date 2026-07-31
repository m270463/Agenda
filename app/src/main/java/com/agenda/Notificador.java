package com.agenda;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Notificador {
    private final ScheduledExecutorService agendador = Executors.newSingleThreadScheduledExecutor();

    public void iniciarVerificacaoDiaria(){
        Runnable tarefa = () -> {
            LocalDate hoje = LocalDate.now();
            ArrayList<Evento> eventosDia = new ArrayList<>();
            for (Usuario u: App.listaUsuarios){
                if (u.getAgenda() != null && u.getAgenda().get(hoje.plusDays(1)) != null){
                     eventosDia.addAll(u.getAgenda().get(hoje.plusDays(1)));
                }
                if (u.getAgendaRepetitiva() != null){
                    for (Evento e: u.getAgendaRepetitiva()){
                        if (e.ocorreEm(hoje.plusDays(1))){
                            eventosDia.add(e);
                        }
                    }
                }
                
                if (!eventosDia.isEmpty()){
                    enviarNotificacao(eventosDia, u, hoje.plusDays(1));
                }

            }
            
        };
        agendador.scheduleAtFixedRate(tarefa, 0, 24, TimeUnit.HOURS);
    }


    private void enviarNotificacao(ArrayList<Evento> eventos, Usuario usuario,LocalDate data){
        String mensagem = "Olá " + usuario.getNome() + ", você tem " + eventos.size() + 
                          " evento(s) programado(s) para " + data.toString() + ":\n";
        
        for (Evento e : eventos) {
            if (e.getHoraInicio() != null)
                mensagem += "- " + e.getNome() + " (das " + e.getHoraInicio() + "às" + e.getHoraFim() +  ")\n";
            else
                mensagem += "- " + e.getNome() + " (durando o dia inteiro)\n";
            
        }

        System.out.println("📧 Disparando e-mail para: " + usuario.getEmail());
        System.out.println(mensagem);
        ServicoEmail.enviarAlerta(usuario.getEmail(), "Lembrete de Eventos", mensagem);
    }

    public void fechar(){
        agendador.close();
    }
}

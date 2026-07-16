package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller responsável pela visualização, edição e remoção de eventos existentes.
 * <p>
 * Esta classe estende {@link controllerEventos} e implementa {@link Validavel}. 
 * Ela permite carregar os dados de um compromisso já salvo para exibição em tela, 
 * gerenciar o modo de leitura/escrita por meio de um botão alternador ({@link ToggleButton}),
 * tratar a atualização das listas de persistência (reorganizando o evento caso ele mude de 
 * recorrente para pontual ou vice-versa) e controlar o fluxo de exclusão segura de registros.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class EditEventoController extends controllerEventos implements Validavel {

    /** A referência do evento que está sendo visualizado ou editado. */
    private Evento evento;
    
    /** Botão usado para confirmar e salvar as alterações feitas no evento. */
    @FXML
    private Button confirmar;
    
    /** Recipiente vertical que agrupa a maioria dos campos do formulário para habilitar/desabilitar em lote. */
    @FXML
    private VBox vbox;
    
    /** Painel de confirmação (modal/overlay) para exclusão do evento. */
    @FXML
    private AnchorPane anchorRemover;

    /** Label que exibe feedback visual em caso de sucesso na edição. */
    @FXML
    private Label confirmacao;

    /** Label que exibe feedback visual em caso de remoção bem-sucedida do evento. */
    @FXML
    private Label confirmacaoErro;

    /** Botão do tipo alternador (Toggle) que ativa ou desativa a permissão de edição nos campos da tela. */
    @FXML
    private ToggleButton botaoEdicao;

    /**
     * Preenche os campos da interface gráfica com as informações do evento selecionado.
     * <p>
     * Trata o estado de eventos de "dia inteiro" (com horário inicial nulo), configurando 
     * adequadamente o interruptor de tempo. Ao final, chama {@link #interruptorEdicao()} 
     * para garantir que a tela inicie no modo de segurança apenas-leitura.
     * </p>
     *
     * @param evento O {@link Evento} cujos dados serão carregados e estruturados na interface.
     */
    public void montarEvento(Evento evento){
        this.evento = evento;

        titulo.setText(evento.getNome());
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        diaInicio.setText(evento.getDiaInicio().format(formatadorData));
        
        if (evento.getHoraInicio() == null){
            btnInterruptor.setSelected(true);
            horaInicio.setManaged(false);
            horaFim.setManaged(false);
            horaInicio.setVisible(false);
            horaFim.setVisible(false);
            erroHoraInicio.setVisible(false);
            erroHoraFim.setVisible(false);
        }
        else{
            DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm");
            horaInicio.setText(evento.getHoraInicio().format(formatadorHora));
            horaFim.setText(evento.getHoraFim().format(formatadorHora));
        }
        descricao.setText(evento.getDescricao());
        Comborepeticao.getSelectionModel().select(evento.getRepeticao());
        interruptorEdicao();
    }

    /**
     * Valida os dados de entrada do formulário de edição de evento.
     * <p>
     * Garante que títulos não fiquem vazios, que as datas informadas sejam reais e formatadas 
     * e que, na ausência da marcação de "dia inteiro", os horários de início e fim sejam coerentes 
     * (não permitindo que o término ocorra antes do início).
     * </p>
     *
     * @return {@code true} se todos os dados inseridos estiverem consistentes; 
     * {@code false} caso ocorra alguma quebra de validação ou erro de preenchimento.
     */
    @Override
    public boolean validar(){
        boolean valido = true;
        if (titulo.getText().isBlank()){
            valido = false;
            erroTitulo.setText("*Campo obrigatório!");
            erroTitulo.setVisible(true);
        }
        else{
            erroTitulo.setVisible(false);
        }

        if (diaInicio.getText().isBlank()){
            valido = false;
            erroDiaInicio.setText("*Campo obrigatório!");
            erroDiaInicio.setVisible(true);
        }
        else if (verificaDateTime(diaInicio.getText())){
            erroDiaInicio.setVisible(false);
        }
        else{
            valido = false;
            erroDiaInicio.setText("*Data inválida!");
            erroDiaInicio.setVisible(true);
        }

        if (!btnInterruptor.isSelected()){

            if (horaInicio.getText().isBlank()){
                valido = false;
                erroHoraInicio.setText("*Campo obrigatório!");
                erroHoraInicio.setVisible(true);
            }
            else if (verificaLocalTime(horaInicio.getText())){
                erroHoraInicio.setVisible(false);
            }
            else{
                valido = false;
                erroHoraInicio.setText("*Hora inválida!");
                erroHoraInicio.setVisible(true);
            }
            
            if (horaFim.getText().isBlank()){
                valido = false;
                erroHoraFim.setText("*Campo obrigatório!");
                erroHoraFim.setVisible(true);
            }
            else if (verificaLocalTime(horaFim.getText())){
                erroHoraFim.setVisible(false);
            }
            else{
                valido = false;
                erroHoraFim.setText("*Hora inválida!");
                erroHoraFim.setVisible(true);
            }

            if (!horaInicio.getText().isBlank() && !horaFim.getText().isBlank() 
                && verificaLocalTime(horaInicio.getText()) && verificaLocalTime(horaFim.getText())){

                LocalTime horaInicial = LocalTime.parse(horaInicio.getText());
                LocalTime horaFinal = LocalTime.parse(horaFim.getText());
                if (horaFinal.isBefore(horaInicial)){
                    valido = false;
                    erroHoraFim.setText("*Horários incompatíveis!");
                    erroHoraFim.setVisible(true);
                }
            }
        }

        if (Comborepeticao.getSelectionModel().getSelectedItem() == null){
            valido = false;
            erroComboRepeticao.setText("*Campo obrigatório!");
            erroComboRepeticao.setVisible(true);
        }
        else{
            erroComboRepeticao.setVisible(false);
        }
        return valido;
    }

    /**
     * Salva as alterações feitas no evento editado.
     * <p>
     * Se os campos forem validados, os atributos do objeto {@link Evento} original são atualizados. 
     * Se houver mudança de categoria do evento (por exemplo: mudar a recorrência de "Nunca" para "Semanalmente" 
     * ou vice-versa), o método transfere automaticamente a instância de evento entre os contêineres do usuário ativo 
     * (de {@code getAgenda()} para {@code getAgendaRepetitiva()}, ou o inverso).
     * </p>
     * <p>
     * Apresenta feedback visual de sucesso e redireciona o usuário em 1 segundo de volta para a visualização do dia.
     * </p>
     */
    @Override
    public void botaoConfirmar(){
        String title = "";
        LocalDate diaComeco= null;
        LocalTime horaComeco = null;
        LocalTime horaTermino = null;
        String repeticao = "";
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String desc = descricao.getText();

        if (validar()){
            title = titulo.getText();
            diaComeco = LocalDate.parse(diaInicio.getText(), formatador);
            if (!btnInterruptor.isSelected()){
                horaComeco = LocalTime.parse(horaInicio.getText());
                horaTermino = LocalTime.parse(horaFim.getText());
            }
            repeticao = Comborepeticao.getValue().toString();

            evento.setNome(title);
            evento.setDescricao(desc);
            evento.setDiaInicio(diaComeco);
            evento.setHoraInicio(horaComeco);
            evento.setHoraFim(horaTermino);

            // Gerencia a transferência de coleções se o tipo de repetição mudar drasticamente
            if (evento.getRepeticao().equals("Nunca") && !repeticao.equals(evento.getRepeticao())){
                App.usuarioaAtivo.getAgendaRepetitiva().add(evento);
                App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).remove(evento);
            }
            else if (!evento.getRepeticao().equals("Nunca") && repeticao.equals("Nunca")){
                App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).add(evento);
                App.usuarioaAtivo.getAgendaRepetitiva().remove(evento);
            }
            evento.setRepeticao(repeticao);
            
            confirmacao.setText("Evento editado!");
            confirmacao.setVisible(true);
            
            PauseTransition pausa = new PauseTransition(Duration.seconds(1));
            pausa.setOnFinished(event ->{
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                    Parent root = loader.load();

                    verEventoController novoController = loader.getController();
                    ArrayList<Evento> lista = new ArrayList<>();
                    
                    if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) != null){
                        lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
                    }
                    for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                        if (e.ocorreEm(evento.getDiaInicio()))
                            lista.add(e);
                    }

                    novoController.carregarLista(lista, evento.getDiaInicio());
                    Stage stage = (Stage) titulo.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    root.applyCss();
                    root.layout();
                    stage.setTitle("Agenda - Calendário");

                } catch (IOException e) {
                    System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                    e.printStackTrace();
                }
            });
            pausa.play();
        }
        else{
            confirmacao.setVisible(false);
        }
    }
    
    /**
     * Abandona as alterações em andamento e retorna o fluxo à lista de compromissos daquele dia.
     */
    @Override
    public void botaoVoltar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
            Parent root = loader.load();

            verEventoController novoController = loader.getController();
            ArrayList<Evento> lista = new ArrayList<>();
            
            if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) != null){
                lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
            }
            for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (e.ocorreEm(evento.getDiaInicio()))
                    lista.add(e);
            }

            novoController.carregarLista(lista, evento.getDiaInicio());
            Stage stage = (Stage) titulo.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Calendário");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Torna visível o painel (ancora) de diálogo para confirmação de remoção do evento.
     */
    @FXML
    private void botaoRemover(){
        anchorRemover.setVisible(true);
    }

    /**
     * Oculta o painel de confirmação de exclusão, cancelando o fluxo de exclusão rápida.
     */
    @FXML
    private void botaoCancelar(){
        anchorRemover.setVisible(false);
    }

    /**
     * Remove de forma definitiva o evento das listas de registros do usuário ativo.
     * <p>
     * Se for um evento de recorrência única, o remove do mapa estruturado de datas. 
     * Caso contrário, remove da lista de repetitivos. Exibe uma confirmação, aguarda 
     * 1 segundo e redireciona de volta para a listagem diária atualizada.
     * </p>
     */
    @FXML
    private void botaoConfirmarRemocao(){
        if (evento.getRepeticao().equals("Nunca")){
            App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()).remove(evento);
        }
        else{
            App.usuarioaAtivo.getAgendaRepetitiva().remove(evento);
        }
        confirmacaoErro.setVisible(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(1));
        pausa.setOnFinished(event ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                Parent root = loader.load();
                verEventoController novoController = loader.getController();
                
                ArrayList<Evento> lista = new ArrayList<>();
                if (App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()) != null){
                    lista.addAll(App.usuarioaAtivo.getAgenda().get(evento.getDiaInicio()));
                }
                for (Evento e: App.usuarioaAtivo.getAgendaRepetitiva()){
                    if (e.ocorreEm(evento.getDiaInicio()))
                        lista.add(e);
                }       

                novoController.carregarLista(lista, evento.getDiaInicio());
                Stage stage = (Stage) confirmacao.getScene().getWindow();
                stage.getScene().setRoot(root);
                root.applyCss();
                root.layout();
                stage.setTitle("Agenda - Login");

            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        });
        pausa.play();
    }

    /**
     * Alterna o estado de edição dos componentes gráficos da tela com base na seleção do {@link ToggleButton}.
     * <p>
     * Se desmarcado, a visualização passa para o modo protegido de apenas-leitura (desabilitando 
     * o container {@code vbox}, a descrição e o botão de confirmação). 
     * Se marcado, os componentes são reabilitados para edição pelo usuário.
     * </p>
     */
    @FXML
    private void interruptorEdicao(){
        if (!botaoEdicao.isSelected()){
            vbox.setDisable(true);
            descricao.setDisable(true);
            confirmar.setDisable(true);
        }
        else{
            vbox.setDisable(false);
            descricao.setDisable(false);
            confirmar.setDisable(false);
        }
    } 
}
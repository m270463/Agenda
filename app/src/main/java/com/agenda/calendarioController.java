package com.agenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Controller responsável pela visualização e interação com a tela de calendário.
 * <p>
 * Gerencia a renderização dos dias do mês selecionado dentro de um {@link GridPane},
 * a seleção dinâmica de mês/ano por meio de {@link ComboBox}, a transição entre telas 
 * (detalhes de eventos, criação de novos eventos e login) e a exibição de badges informando 
 * a quantidade de compromissos de cada dia (incluindo eventos recorrentes).
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class calendarioController {

    /**
     * Grade visual que estrutura o calendário, onde cada célula representará um dia do mês.
     */
    @FXML
    private GridPane calendario;

    /**
     * Botão para retornar ao mês anterior.
     */
    @FXML
    private Button voltar;

    /**
     * Botão para avançar para o próximo mês.
     */
    @FXML
    private Button avancar;

    /**
     * Caixa de seleção contendo os nomes dos meses em formato textual.
     */
    @FXML
    private ComboBox<String> comboMes;

    /**
     * Caixa de seleção contendo os anos representados em formato String.
     */
    @FXML
    private ComboBox<String> comboAno;


    /**
     * O mês e o ano atualmente selecionados e exibidos na interface do usuário.
     */
    private YearMonth mesAtual;

    /**
     * Inicializa o controller logo após o carregamento do arquivo FXML.
     * <p>
     * Configura a data inicial como o mês corrente, popula as caixas de seleção 
     * (meses em português e anos de 1970 a 2050), estiliza os botões internos de seta 
     * dos combos, define os listeners de eventos de seleção e invoca a montagem visual 
     * do calendário.
     * </p>
     */
    @FXML
    public void initialize() {
        mesAtual = YearMonth.now();

        // Popula a lista com os meses localizados em português do Brasil
        List<String> meses = new ArrayList<>();
        Locale localBR = new Locale("pt", "BR");
        for (int i = 1; i <= 12; i++) {
            String nomeMes = java.time.Month.of(i).getDisplayName(TextStyle.FULL, localBR);
            nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
            meses.add(nomeMes);
        }
        comboMes.setItems(FXCollections.observableArrayList(meses));
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1);

        // Define renderizadores personalizados para a aparência das células do combo
        comboMes.setButtonCell(criarCelulaCustomizada(false)); 
        comboMes.setCellFactory(lv -> criarCelulaCustomizada(true)); 

        // Popula a lista com o intervalo de anos (1970 - 2050)
        List<String> anos = new ArrayList<>();
        for (int i = 1970; i <= 2050; i++){
            anos.add(String.valueOf(i));
        }

        comboAno.setItems(FXCollections.observableArrayList(anos));
        comboAno.setValue(String.valueOf(mesAtual.getYear()));

        comboAno.setButtonCell(criarCelulaCustomizada(false));
        comboAno.setCellFactory(lv -> criarCelulaCustomizada(true));

        // Customização CSS das setas de dropdown (arrows) dos ComboBoxes após renderização em tela
        Platform.runLater(() -> {
            Node arrowButton = comboMes.lookup(".arrow-button");
            if (arrowButton != null) {
                arrowButton.setStyle("-fx-background-color: #1c1d22; -fx-border-color:  white white white transparent");
            }
            Node arrow = comboMes.lookup(".arrow");
            if (arrow != null) {
                arrow.setStyle("-fx-background-color: white;");
            }

            Node arrowButton1 = comboAno.lookup(".arrow-button");
            if (arrowButton1 != null) {
                arrowButton1.setStyle("-fx-background-color: #1c1d22; -fx-border-color:  white white white transparent");
            }
            Node arrow1 = comboAno.lookup(".arrow");
            if (arrow1 != null) {
                arrow1.setStyle("-fx-background-color: white;");
            }
        });

        // Configura a alteração automática do calendário ao mudar o mês
        comboMes.setOnAction(event -> {
            int mesSelecionado = comboMes.getSelectionModel().getSelectedIndex() + 1;
            mesAtual = YearMonth.of(mesAtual.getYear(), mesSelecionado);
            montarCalendario(mesAtual);
        });

        // Configura a alteração automática do calendário ao mudar o ano
        comboAno.setOnAction(event -> {
            int anoSelecionado = Integer.parseInt(comboAno.getValue());
            mesAtual = YearMonth.of(anoSelecionado, comboMes.getSelectionModel().getSelectedIndex() + 1);
            montarCalendario(mesAtual);
        });

        comboMes.setVisibleRowCount(12);
        comboAno.setVisibleRowCount(10);
        montarCalendario(mesAtual);
    }

    /**
     * Cria e formata uma célula customizada de texto para os ComboBoxes do sistema.
     * * @param celula {@code true} se a célula for renderizada na lista suspensa (dropdown); 
     * {@code false} se for a célula que exibe o item atualmente selecionado no botão.
     * @return Uma instância configurada de {@link ListCell} para renderizar itens de texto.
     */
    private ListCell<String> criarCelulaCustomizada(boolean celula) {
        return new ListCell<>() {   
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else if (celula){
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-alignment: CENTER; -fx-background-color:  #1c1d22;");
                }
                else{
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-alignment: CENTER; -fx-background-color:  #1c1d22; -fx-border-color:   white  transparent white white");
                }
            }
        };
    }

    /**
     * Evento acionado pelo botão voltar. Retrocede o calendário em exatamente um mês 
     * e atualiza os componentes de seleção visual.
     */
    @FXML
    private void botaoVoltar(){
        mesAtual = mesAtual.minusMonths(1);
        montarCalendario(mesAtual);
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1 % 12);
        comboAno.setValue(String.valueOf(mesAtual.getYear()));
    }

    /**
     * Retorna o fluxo da aplicação para a tela inicial/login.
     * Carrega a cena definida pelo FXML {@code teste.fxml}.
     */
    @FXML
    private void botaovoltarInicio(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) avancar.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Login");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Evento acionado pelo botão avançar. Avança o calendário em exatamente um mês 
     * e atualiza os componentes de seleção visual.
     */
    @FXML
    private void botaoAvancar(){
        mesAtual = mesAtual.plusMonths(1);
        montarCalendario(mesAtual);
        comboMes.getSelectionModel().select(mesAtual.getMonthValue() - 1 % 12);
        comboAno.setValue(String.valueOf(mesAtual.getYear()));
    }

    /**
     * Reconstrói dinamicamente os componentes visuais dos dias no {@link GridPane} com base no mês informado.
     * <p>
     * Este método limpa todas as linhas de dias existentes na grade, determina a coluna correta para 
     * o primeiro dia do mês de acordo com o dia da semana, cria elementos de interface contendo botões 
     * para cada dia, destaca o dia corrente e anexa badges indicativos caso existam eventos pontuais 
     * ou repetitivos cadastrados para a data correspondente. Adicionalmente, configura o comportamento de 
     * clique em cada dia para abrir a tela de visualização de detalhes dos eventos.
     * </p>
     *
     * @param mesAtual O {@link YearMonth} representando o mês que deve ser montado na tela.
     */
    private void montarCalendario(YearMonth mesAtual){
        
        // Remove os componentes visuais antigos de dias, mantendo apenas o cabeçalho (linha 0)
        calendario.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            int linhaAtual = (rowIndex == null) ? 0 : rowIndex;
            return linhaAtual > 0;
        });

        LocalDate primeirodiaMes = mesAtual.atDay(1);
        int diaSemana = primeirodiaMes.getDayOfWeek().getValue();
        int colunaInicial = 0;
        if (diaSemana != 7) // Tratamento para que o Domingo (7) comece na coluna 0
            colunaInicial = diaSemana;
        
        int totalDias = mesAtual.lengthOfMonth();

        int linha = 1;
        int coluna = colunaInicial;
        for (int dia = 1; dia <= totalDias; dia++){
            StackPane stack = new StackPane();
            stack.setAlignment(Pos.CENTER_LEFT);
            stack.setMaxHeight(Double.MAX_VALUE);
            stack.setMaxWidth(Double.MAX_VALUE);
            stack.setStyle("-fx-background-color: #121212;-fx-border-color: #333333");
            
            Button botaoDia = new Button(String.valueOf(dia));
            botaoDia.setMaxWidth(Double.MAX_VALUE);
            botaoDia.setMaxHeight(Double.MAX_VALUE);
            botaoDia.setStyle(
                "-fx-border-color: transparent; " +    
                "-fx-border-width: 0.5px; " +       
                "-fx-background-color: transparent; " + 
                "-fx-text-fill: white;"  +           
                "-fx-alignment: top-left; " +       
                "-fx-padding: 4px 0px 0px 6px;"     
            );

            LocalDate dataBotao = mesAtual.atDay(dia);

            // Destaca a borda da célula caso ela represente a data atual do sistema (Hoje)
            if (dataBotao.isEqual(LocalDate.now())) {
                stack.setStyle(
                    "-fx-border-color: #00adb5; " +    
                    "-fx-border-width: 1.5px; " +       
                    "-fx-background-color: #1a1a1a; " 
                );
            }
            stack.getChildren().add(botaoDia);
            
            ArrayList<Evento> eventosDia = new ArrayList<>();
            // Verifica e adiciona eventos pontuais agendados para a data
            if (App.usuarioaAtivo.getAgenda().get(dataBotao) != null)
                eventosDia.addAll(App.usuarioaAtivo.getAgenda().get(dataBotao));
            
            // Filtra e adiciona eventos com lógica de repetição que ocorram nesta data
            for (Evento evento: App.usuarioaAtivo.getAgendaRepetitiva()){
                if (evento.ocorreEm(dataBotao))
                    eventosDia.add(evento);
            }

            // Adiciona um selo visual (badge) vermelho indicando o número de compromissos
            if (!eventosDia.isEmpty()){
                Label badgeContador = new Label(String.valueOf(eventosDia.size()));
                badgeContador.setStyle(
                    "-fx-background-color: #FF0000; " + 
                    "-fx-text-fill: white; " +         
                    "-fx-font-size: 9px; " +           
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 2px 5px 2px 5px; " +  
                    "-fx-background-radius: 10px;"+
                    "fx-alignment: top-right"      
                );
                stack.getChildren().add(badgeContador);    
                stack.setAlignment(badgeContador, Pos.TOP_RIGHT);
            }
            
            final ArrayList<Evento> eventosNoDia = eventosDia;
            final LocalDate dataDia = dataBotao;
            
            // Configura o clique no botão do dia para transicionar para a tela de visualização de eventos
            botaoDia.setOnAction(event ->{
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("verevento.fxml"));
                    Parent root = loader.load();

                    verEventoController novoController = loader.getController();
                    novoController.carregarLista(eventosNoDia, dataDia);
                    
                    Stage stage = (Stage) comboMes.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    root.applyCss();
                    root.layout();
                    stage.setTitle("Agenda - Calendário");

                } catch (IOException e) {
                    System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                    e.printStackTrace();
                }
            });

            calendario.add(stack, coluna, linha);
            coluna++;

            if (coluna > 6){
                coluna = 0;
                linha++;
            }
        }
    }

    /**
     * Transiciona a aplicação para a tela de criação de novos eventos.
     * Carrega a cena definida pelo arquivo FXML {@code criaevento.fxml}.
     */
    @FXML
    private void botaoAdicionar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("criaevento.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) comboAno.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Novo Evento");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }
}
package com.agenda;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller responsável pela tela de criação de novas contas de usuário.
 * <p>
 * Implementa a interface {@link Validavel} para gerenciar a validação em tempo real de formulários.
 * Controla o fluxo de cadastro validando nomes, e-mails (restritos temporariamente ao domínio Gmail),
 * formatação dinâmica de telefones brasileiros, consistência de senhas, além de oferecer navegação guiada por teclado (Enter).
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class CriacontaController implements Validavel {

    /** Botão para cancelar o cadastro e retornar à tela anterior. */
    @FXML
    private Button botaoVoltar;
    
    /** Campo de texto destinado à inserção do nome completo do usuário. */
    @FXML
    private TextField campoNome;

    /** Mensagem de erro exibida quando o campo nome está inconsistente ou em branco. */
    @FXML
    private Label erroNome;

    /** Campo de texto para inserção do endereço de e-mail. */
    @FXML
    private TextField campoEmail;

    /** Mensagem de erro correspondente a falhas de preenchimento ou formato do e-mail. */
    @FXML
    private Label erroEmail;

    /** Campo de texto formatado com máscara de entrada dinâmica para telefone celular. */
    @FXML
    private TextField campoTelefone;

    /** Mensagem de erro para indicar telefones incompletos ou vazios. */
    @FXML
    private Label erroTelefone;

    /** Campo de senha para inserção do código de acesso da conta. */
    @FXML
    private TextField campoSenha;

    /** Mensagem de erro que avisa a ausência de digitação no campo de senha. */
    @FXML
    private Label erroSenha;

    /** Campo de texto para confirmação e dupla checagem da senha digitada. */
    @FXML
    private TextField campoconfirmaSenha;

    /** Mensagem de erro exibida caso as senhas digitadas não coincidam. */
    @FXML
    private Label erroconfirmaSenha;

    /** Label de notificação temporária exibida para confirmar o sucesso na criação da conta. */
    @FXML
    private Label confirmaCadastro;

    /** Painel raiz da interface usado para retirar o foco inicial automático dos campos. */
    @FXML
    private AnchorPane anchor;

    /**
     * Valida de forma restrita se o e-mail informado pertence ao domínio "@gmail.com".
     *
     * @param email O endereço de e-mail a ser inspecionado.
     * @return {@code true} se o e-mail possuir caracteres válidos seguidos exatamente pelo sufixo @gmail.com; 
     * {@code false} caso contrário ou se for nulo.
     */
    private boolean validaEmail(String email) {
        if (email == null) {
            return false;
        }
        for (int i = 0; i < email.length(); i++){
            if (i + 1 < email.length() - 1 && email.charAt(i) == '@' && email.substring(i + 1).equals("gmail.com"))
                return true;      
        }
        return false;
    }

    /**
     * Valida todos os campos do formulário de criação de conta simultaneamente.
     * <p>
     * Garante o preenchimento dos campos obrigatórios, consistência do formato de e-mail,
     * completude da máscara de telefone brasileira (padrão de 14 dígitos formatados) e 
     * igualdade estrita entre as senhas inseridas.
     * </p>
     *
     * @return {@code true} se todos os dados do formulário estiverem perfeitamente consistentes; 
     * {@code false} se houver qualquer erro de validação.
     */
    @Override
    public boolean validar() {
        boolean valido = true;

        if (campoNome.getText().trim().isEmpty()){
            erroNome.setText("*Campo obrigatório!");
            erroNome.setVisible(true);
            valido = false;
        }
        else
            erroNome.setVisible(false);

        String email = campoEmail.getText().trim();

        if (email.isEmpty()){
            erroEmail.setText("*Campo obrigatório!");
            erroEmail.setVisible(true); 
            valido = false;
        }
        else if(!validaEmail(email)){
            erroEmail.setText("*Email inválido!");
            erroEmail.setVisible(true);
            valido = false;
        }
        else
            erroEmail.setVisible(false);
        

        if (campoTelefone.getText().replace(" ","").length() != 14){
            erroTelefone.setText("*Telefone inválido!");
            erroTelefone.setVisible(true);
            valido = false;
        }
        else if (campoTelefone.getText().isBlank()){
            erroTelefone.setText("Campo obrigatório!");
            erroTelefone.setVisible(true);
            valido = false;
        }
        else
            erroTelefone.setVisible(false);

        String senha = campoSenha.getText();
        String confirmaSenha = campoconfirmaSenha.getText();

        if (senha.isEmpty()){
            erroSenha.setText("*Campo obrigatório!");
            erroSenha.setVisible(true);
            valido = false;
        }
        else
            erroSenha.setVisible(false);

        if (!senha.equals(confirmaSenha)){
            erroconfirmaSenha.setText("Senhas incompativeis!");
            erroconfirmaSenha.setVisible(true);
            valido = false;
        }
        else{
            erroconfirmaSenha.setVisible(false);
        }

        return valido;
    }

    /**
     * Inicializa a tela de cadastro logo após o carregamento do FXML.
     * <p>
     * Vincula a propriedade de gerenciamento de layout ({@code managed}) de todas as labels de erro 
     * às suas respectivas propriedades {@code visible} para evitar lacunas na interface gráfica.
     * Define foco inicial no painel principal, atribui o formatador de telefone e programa 
     * a navegação sequencial entre os campos de texto através da tecla <b>Enter</b>.
     * </p>
     */
    @FXML
    private void initialize(){
        erroNome.managedProperty().bind(erroNome.visibleProperty());
        erroEmail.managedProperty().bind(erroEmail.visibleProperty());
        erroTelefone.managedProperty().bind(erroTelefone.visibleProperty());
        erroSenha.managedProperty().bind(erroSenha.visibleProperty());
        erroconfirmaSenha.managedProperty().bind(erroconfirmaSenha.visibleProperty());
        confirmaCadastro.managedProperty().bind(confirmaCadastro.visibleProperty());

        Platform.runLater(() -> anchor.requestFocus());
        formataTelefone(campoTelefone);

        // Define a lógica de navegação usando o Enter entre os campos de formulário
        campoNome.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoEmail.requestFocus(); 
                event.consume(); 
            }
        });

        campoEmail.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoTelefone.requestFocus();
                event.consume();
            }
        });

        campoTelefone.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoSenha.requestFocus(); 
                event.consume(); 
            }
        });

        campoSenha.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                campoconfirmaSenha.requestFocus(); 
                event.consume(); 
            }
        });

        campoconfirmaSenha.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                aoClicarbotaoCadastrar();
                event.consume(); 
            }
        });
    } 

    /**
     * Trata o evento de cadastro do usuário.
     * <p>
     * Se os campos passarem na validação do método {@link #validar()}, o novo usuário é criado e adicionado 
     * à lista estática global {@code App.listaUsuarios}. Uma notificação temporária de confirmação é exibida 
     * e após 1 segundo ocorre o redirecionamento automático para a tela de login.
     * </p>
     */
    @FXML
    private void aoClicarbotaoCadastrar(){
        if (!validar())
            return;
            
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String telefone = campoTelefone.getText();
        String senha = campoSenha.getText();
        
        Usuario newUser = new Usuario(nome, email, telefone, senha);
        App.listaUsuarios.add(newUser);
        confirmaCadastro.setText("Conta criada!");
        confirmaCadastro.setVisible(true);

        // Cria uma pausa de 1 segundo antes de redirecionar o usuário
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));
        pausa.setOnFinished(event ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoSenha.getScene().getWindow();
                Scene novaCena = new Scene(root);

                stage.setScene(novaCena);
                stage.setTitle("Agenda - Login");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
                e.printStackTrace();
            }
        });
        pausa.play();
    }
   
    /**
     * Interrompe o processo atual de cadastro e retorna o usuário de volta à tela inicial/login.
     */
    @FXML
    private void aoClicarBotaoVoltar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) botaoVoltar.getScene().getWindow();
            Scene novaCena = new Scene(root);

            stage.setScene(novaCena);
            stage.setTitle("Agenda - Início");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Adiciona filtros e regras de reformatação dinâmica em tempo real para um campo de telefone.
     * <p>
     * Formata os dígitos numéricos inseridos de acordo com o padrão nacional brasileiro. 
     * Conforme a digitação progride, aplica as decorações do DDD e do traço separador, resultando em: 
     * {@code (XX) XXXXX-XXXX}.
     * </p>
     *
     * @param campoTelefone O {@link TextField} que receberá o filtro de máscara telefônica.
     */
    private void formataTelefone(TextField campoTelefone){
        campoTelefone.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isDeleted() || change.getText().isEmpty()) {
                return change;
            }

            if (!change.getText().matches("[0-9]*")) {
                return null;
            }

            String textoFuturo = change.getControlNewText();

            if (textoFuturo.length() > 15) {
                return null;
            }

            if (textoFuturo.length() == 1) {
                change.setText("(" + change.getText());
                int novaPosicao = change.getCaretPosition() + 1;
                change.setCaretPosition(novaPosicao);
                change.setAnchor(novaPosicao);
            }

            if (textoFuturo.length() == 3) {
                change.setText(change.getText() +  ") ");
                int novaPosicao = change.getCaretPosition() + 2;
                change.setCaretPosition(novaPosicao);
                change.setAnchor(novaPosicao);
            }

            if (textoFuturo.length() == 4) {
                change.setText(") " + change.getText() );
                int novaPosicao = change.getCaretPosition() + 2;
                change.setCaretPosition(novaPosicao);
                change.setAnchor(novaPosicao);
            }

            if (textoFuturo.length() == 10){
                change.setText(change.getText() +  "-");
                int novaPosicao = change.getCaretPosition() + 1;
                change.setCaretPosition(novaPosicao);
                change.setAnchor(novaPosicao);
            }

            if (textoFuturo.length() == 11) {
                change.setText("-" + change.getText() );
                int novaPosicao = change.getCaretPosition() + 1;
                change.setCaretPosition(novaPosicao);
                change.setAnchor(novaPosicao);
            }

            return change;
        }));
    }
}
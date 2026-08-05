package com.agenda;


import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class editarUsuarioController implements Validavel{

/**
 * Controller responsável pela tela de criação de novas contas de usuário.
 * <p>
 * Implementa a interface {@link Validavel} para garantir que os dados inseridos 
 * no formulário sejam verificados antes de criar um novo registro. Esta classe gerencia 
 * a interface gráfica, aplica máscaras de formatação de telefone, valida regras de 
 * negócio (como exclusividade de e-mail e compatibilidade de senhas) e controla o 
 * redirecionamento de telas.
 * </p>
 */


    /** Botão utilizado para cancelar o cadastro e voltar à tela inicial de login. */
    @FXML
    private Button botaoVoltar;
    
    /** Campo de entrada de texto destinado ao nome completo do usuário. */
    @FXML
    private TextField campoNome;

    /** Rótulo (label) para exibir mensagens de erro associadas ao campo de nome. */
    @FXML
    private Label erroNome;

    /** Campo de entrada de texto destinado ao e-mail do usuário. */
    @FXML
    private TextField campoEmail;

    /** Rótulo (label) para exibir mensagens de erro associadas ao campo de e-mail. */
    @FXML
    private Label erroEmail;

    /** Campo de entrada de texto destinado ao telefone do usuário, com máscara dinâmica. */
    @FXML
    private TextField campoTelefone;

    /** Rótulo (label) para exibir mensagens de erro associadas ao campo de telefone. */
    @FXML
    private Label erroTelefone;

    /** Campo de entrada de texto protegido (senha) destinado à senha do usuário. */
    @FXML
    private TextField campoSenha; // Nota: Recomenda-se utilizar PasswordField para senhas

    /** Rótulo (label) para exibir mensagens de erro associadas ao campo de senha. */
    @FXML
    private Label erroSenha;

    /** Campo de entrada de texto protegido para a confirmação da senha do usuário. */
    @FXML
    private TextField campoconfirmaSenha; // Nota: Recomenda-se utilizar PasswordField

    /** Rótulo (label) para exibir mensagens de erro associadas à confirmação de senha. */
    @FXML
    private Label erroconfirmaSenha;

    /** Rótulo (label) de feedback positivo exibido após a criação bem-sucedida da conta. */
    @FXML
    private Label confirmaCadastro;

    /** Painel raiz da interface gráfica da tela de criação de conta. */
    @FXML
    private AnchorPane anchor;

    @FXML
    private AnchorPane anchorCodigo;

    @FXML
    private AnchorPane anchorRemocao;

    @FXML
    private TextField campoCodigo;

    @FXML
    private  Label erroCodigo;

    @FXML
    private ToggleButton botaoEdicao;

    @FXML
    private VBox vbox;
    
    private int codigo;


    private int qtdErros = 0;


    /**
     * Valida se o e-mail possui um formato aceito pelo sistema.
     * <p>
     * Atualmente, a validação restringe o cadastro apenas a e-mails do domínio "gmail.com".
     * </p>
     *
     * @param email A string contendo o e-mail a ser validado.
     * @return {@code true} se o e-mail for válido e pertencer ao domínio esperado; {@code false} caso contrário.
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
     * Verifica se o e-mail informado já está cadastrado no sistema.
     * <p>
     * O método percorre a lista de usuários armazenada em {@link App#listaUsuarios} 
     * e compara os e-mails, ignorando diferenças entre maiúsculas e minúsculas.
     * </p>
     *
     * @param email A string contendo o e-mail a ser verificado.
     * @return {@code true} se o e-mail já existir na base de dados; {@code false} caso contrário.
     */
    private boolean emailJaCadastrado(String email) {
        for (Usuario u : App.listaUsuarios) {

            if (u.getEmail().equals(email) && !u.getEmail().equals(App.usuarioaAtivo.getEmail())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida todos os campos do formulário de criação de conta.
     * <p>
     * Verifica campos em branco, formatação e exclusividade de e-mail, tamanho e formato 
     * do telefone e a equivalência entre os campos de senha e confirmação de senha. 
     * Controla também a visibilidade dos rótulos (labels) de erro correspondentes.
     * </p>
     *
     * @return {@code true} se todos os dados do formulário forem consistentes e válidos; {@code false} caso contrário.
     */
    @Override
    public boolean validar(){
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
        else if(!validaEmail(email) || !ValidadorDominio.dominioPossuiServidorEmail(email)){
            erroEmail.setText("*Email inválido!");
            erroEmail.setVisible(true);
            valido = false;
        }
        else if(emailJaCadastrado(email)){
            erroEmail.setText("*Email já cadastrado!");
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
     * Método executado automaticamente pela estrutura do JavaFX logo após o carregamento do arquivo FXML.
     * <p>
     * É responsável por realizar configurações iniciais como:
     * <ul>
     * <li>Vincular a propriedade de renderização (`managed`) das mensagens de erro à sua visibilidade, 
     * garantindo que não ocupem espaço no layout quando invisíveis.</li>
     * <li>Aplicar a máscara de formatação do telefone.</li>
     * <li>Configurar a navegação por teclado (foco muda de campo ao pressionar "ENTER").</li>
     * </ul>
     * </p>
     */
    @FXML
    private void initialize(){
        // Labels de erro só ocupam espaço no layout quando estão visíveis
        erroNome.managedProperty().bind(erroNome.visibleProperty());
        erroEmail.managedProperty().bind(erroEmail.visibleProperty());
        erroTelefone.managedProperty().bind(erroTelefone.visibleProperty());
        erroSenha.managedProperty().bind(erroSenha.visibleProperty());
        erroconfirmaSenha.managedProperty().bind(erroconfirmaSenha.visibleProperty());
        confirmaCadastro.managedProperty().bind(confirmaCadastro.visibleProperty());

        Platform.runLater(() ->anchor.requestFocus());
        formataTelefone(campoTelefone);
        formataCampoCodigo(campoCodigo);
        
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

        campoNome.setText(App.usuarioaAtivo.getNome());
        campoEmail.setText(App.usuarioaAtivo.getEmail());
        campoTelefone.setText(App.usuarioaAtivo.getTelefone());
        campoSenha.setText(App.usuarioaAtivo.getSenha());
        campoconfirmaSenha.setText(App.usuarioaAtivo.getSenha());

        interruptorEdicao();
        
    } 

    private int enviarCodigo(){
        int codigo = ThreadLocalRandom.current().nextInt(10000, 100000);
        String mensagem = "Código para confirmar alterações na conta: " + codigo;
        ServicoEmail.enviarAlerta(App.usuarioaAtivo.getEmail(), "Solicitação de alterações na conta", mensagem);
        return codigo;
    }

    private boolean verificarCodigo(int codigo){
        if (campoCodigo.getText().isBlank())
            return false;
        if (Integer.parseInt(campoCodigo.getText()) != codigo)
           return false;
        return true;
            

        
    }

    /**
     * Acionado quando o usuário clica no botão para finalizar o cadastro ou aperta "ENTER" no último campo.
     * <p>
     * Se os dados passarem pela validação do método {@link #validar()}, o novo usuário é instanciado 
     * e adicionado à lista global em {@link App#listaUsuarios}. Em seguida, exibe uma mensagem de 
     * sucesso e, após um intervalo de 1 segundo, redireciona o usuário para a tela de login.
     * </p>
     */
    @FXML
    private void aoClicarbotaoCadastrar(){
        if (!validar())
            return;
        
        anchorCodigo.setVisible(true);
        new Thread(() -> {
        this.codigo = enviarCodigo(); 
        }).start();

    }
   
    /**
     * Acionado ao clicar no botão "Voltar". Cancela o fluxo de criação de conta e redireciona 
     * o usuário de volta à tela inicial de login ({@code teste.fxml}).
     */
    @FXML
    private void aoClicarBotaoVoltar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("calendario.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) botaoVoltar.getScene().getWindow();
            stage.getScene().setRoot(root);
            root.applyCss();
            root.layout();
            stage.setTitle("Agenda - Início");

        } catch (IOException e) {
            System.err.println("Erro crítico ao carregar o arquivo da nova cena!");
            e.printStackTrace();
        }
    }

    /**
     * Configura um formatador de texto dinâmico para o campo de telefone.
     * <p>
     * Aplica uma máscara ao campo enquanto o usuário digita, formatando os números 
     * no padrão brasileiro de telefonia celular com DDD: {@code (XX) XXXXX-XXXX}. 
     * Bloqueia a inserção de caracteres não numéricos.
     * </p>
     *
     * @param campoTelefone O componente {@link TextField} que receberá o formatador e a máscara.
     */
    private void formataTelefone(TextField campoTelefone){
        campoTelefone.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isDeleted() || change.getText().isEmpty() || change.getText().length() == "(11) 99999-9999".length()) {
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

    @FXML 
    private void botaoConfirmarCodigo(){
        PauseTransition pausa = new PauseTransition(Duration.seconds(2));
        if (!verificarCodigo(this.codigo)) {
            erroCodigo.setText("Código Inválido!");
            erroCodigo.setStyle("-fx-text-fill: red;");
            anchorCodigo.setLeftAnchor(erroCodigo, 1000.0);
            anchorCodigo.setRightAnchor(erroCodigo,0.0);
            erroCodigo.setVisible(true);
            qtdErros++;
            if (qtdErros == 3){
                pausa.setOnFinished(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) campoSenha.getScene().getWindow();
                    stage.getScene().setRoot(root);
                    root.applyCss();
                    root.layout();
                    stage.setTitle("Agenda - Login");

                } catch (IOException e) {
                    System.err.println("Erro crítico ao carregar a tela após edição!");
                    e.printStackTrace();
                }
                });
                pausa.play();
            }
            return; 
        }

        qtdErros = 0;
        App.usuarioaAtivo.setNome(campoNome.getText().trim());
        App.usuarioaAtivo.setEmail(campoEmail.getText().trim());
        App.usuarioaAtivo.SetTelefone(campoTelefone.getText().trim());
        App.usuarioaAtivo.setSenha(campoSenha.getText());

        anchorCodigo.setVisible(false); 
        confirmaCadastro.setText("Conta Editada com Sucesso!");
        confirmaCadastro.setVisible(true);
        App.gerenciadorDados.editarUsuario(App.usuarioaAtivo);
        
        pausa.setOnFinished(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoSenha.getScene().getWindow();
                stage.getScene().setRoot(root);
                root.applyCss();
                root.layout();
                stage.setTitle("Agenda - Login");

            } catch (IOException e) {
                System.err.println("Erro crítico ao carregar a tela após edição!");
                e.printStackTrace();
            }
        });
        pausa.play();
    }

    @FXML
    private void botaoCancelarCodigo(){
        anchorCodigo.setVisible(false);
    }
    @FXML
    private void interruptorEdicao(){
        if (!botaoEdicao.isSelected()){
            vbox.setDisable(true);

        }   
        else{
            vbox.setDisable(false);

        }
    } 
    private void formataCampoCodigo(TextField campoCodigo){
        campoCodigo.setTextFormatter(new TextFormatter<>(change -> {
            
            if (!change.getText().matches("[0-9]*")) {
                return null;
                
            }

            String textoFuturo = change.getControlNewText();

            if (textoFuturo.length() > 5) {
                return null;
            }



            return change;
        }));
    }
}


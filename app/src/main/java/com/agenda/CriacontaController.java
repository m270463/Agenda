package com.agenda;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
 * Implementa a interface {@link Validavel} para garantir que os dados inseridos 
 * no formulário sejam verificados antes de criar um novo registro. Esta classe gerencia 
 * a interface gráfica, aplica máscaras de formatação de telefone, valida regras de 
 * negócio (como exclusividade de e-mail e compatibilidade de senhas) e controla o 
 * redirecionamento de telas.
 * </p>
 */
public class CriacontaController implements Validavel {

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
            if (u.getEmail().equalsIgnoreCase(email)) {
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
        else if (emailJaCadastrado(email)){
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
            
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String telefone = campoTelefone.getText();
        String senha = campoSenha.getText();
        
        Usuario newUser = new Usuario(nome, email, telefone, senha);
        App.listaUsuarios.add(newUser);
        
        confirmaCadastro.setText("Conta criada!");
        confirmaCadastro.setVisible(true);
        
        PauseTransition pausa = new PauseTransition(Duration.seconds(1));

        pausa.setOnFinished(event ->{
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) campoSenha.getScene().getWindow();
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
     * Acionado ao clicar no botão "Voltar". Cancela o fluxo de criação de conta e redireciona 
     * o usuário de volta à tela inicial de login ({@code teste.fxml}).
     */
    @FXML
    private void aoClicarBotaoVoltar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("teste.fxml"));
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
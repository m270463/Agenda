/**
 * Definição do módulo principal da aplicação de Agenda.
 * <p>
 * Este módulo estrutura a aplicação utilizando o Java Platform Module System (JPMS).
 * Ele declara as dependências de biblioteca necessárias para a execução da interface gráfica e 
 * do mecanismo de persistência, além de expor pacotes e conceder permissões de acesso reflexivo 
 * para os frameworks externos que sustentam o sistema.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
module com.agenda {
    
    /** * Requer o módulo de controles base do JavaFX (necessário para Buttons, Labels, ComboBoxes, etc.). 
     */
    requires javafx.controls;
    
    /** * Requer o módulo JavaFX FXML, necessário para inflar e carregar as telas a partir de arquivos declarativos .fxml. 
     */
    requires javafx.fxml;
    
    /** * Requer a biblioteca ControlsFX, que fornece componentes visuais estendidos e controles avançados de UI. 
     */
    requires org.controlsfx.controls;
    
    /** * Requer a biblioteca Gson da Google, utilizada para converter objetos Java para arquivos de persistência JSON e vice-versa. 
     */
    requires com.google.gson; 

    requires jakarta.mail;
    
    requires java.naming;

    requires io.github.cdimascio.dotenv.java;
    
    requires java.sql;

    requires java.desktop;
    
    requires org.xerial.sqlitejdbc;
    /**
     * Abre as classes do pacote {@code com.agenda} para acesso reflexivo em tempo de execução.
     * <p>
     * Essa diretiva é estritamente obrigatória por dois motivos fundamentais:
     * </p>
     * <ul>
     * <li><b>{@code javafx.fxml}:</b> Permite que o carregador FXML encontre, instancie e injete elementos privados anotados com {@code @FXML} dentro dos seus controllers JavaFX.</li>
     * <li><b>{@code com.google.gson}:</b> Concede ao Gson permissão para ler e gravar dados em campos privados de classes de modelo (como {@link Usuario} e {@link Evento}) sem a necessidade de expor setters públicos para todos os atributos.</li>
     * </ul>
     */
opens com.agenda to javafx.fxml, com.google.gson;
    /**
     * Exporta o pacote {@code com.agenda} para que seja visível a outros módulos do ecossistema Java.
     * Torna as classes públicas deste pacote utilizáveis pela máquina virtual Java (JVM) e pelo ciclo de vida do JavaFX.
     */
    exports com.agenda;
}
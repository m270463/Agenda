package com.agenda;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Gerenciador responsável pela persistência e leitura de dados dos usuários em formato JSON.
 * <p>
 * Implementa a interface {@link Persistivel} para a classe {@link Usuario}. 
 * Utiliza a biblioteca Gson da Google para converter objetos Java em strings JSON e vice-versa.
 * A classe configura adaptadores de tipos personalizados (<i>Type Adapters</i>) para serializar e desserializar 
 * as classes da API de data e hora do Java 8 ({@link LocalDate}, {@link java.time.LocalTime} e {@link java.time.LocalDateTime}), 
 * utilizando os padrões ISO oficiais.
 * </p>
 * * @author Seu Nome
 * @version 1.0
 */
public class GerenciadorDados implements Persistivel<Usuario> {

    /**
     * O caminho do arquivo físico onde os dados dos usuários serão persistidos.
     * O arquivo {@code usuarios.json} é armazenado no diretório de execução da aplicação.
     */
    private static final Path CAMINHO = Paths.get("usuarios.json");

    /**
     * Instância única pré-configurada do motor Gson.
     * <p>
     * Conta com serializadores e desserializadores customizados para:
     * </p>
     * <ul>
     * <li>{@link LocalDate} usando o formato {@link DateTimeFormatter#ISO_LOCAL_DATE} (yyyy-MM-dd)</li>
     * <li>{@link java.time.LocalTime} usando o formato {@link DateTimeFormatter#ISO_LOCAL_TIME} (HH:mm:ss)</li>
     * <li>{@link java.time.LocalDateTime} usando o formato {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME} (yyyy-MM-ddTHH:mm:ss)</li>
     * </ul>
     * <p>
     * Também ativa a formatação visual amigável (<i>Pretty Printing</i>) no arquivo gerado.
     * </p>
     */
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
            new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
        .registerTypeAdapter(LocalDate.class, (com.google.gson.JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
            LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))

        .registerTypeAdapter(java.time.LocalTime.class, (com.google.gson.JsonSerializer<java.time.LocalTime>) (src, typeOfSrc, context) ->
            new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME)))
        .registerTypeAdapter(java.time.LocalTime.class, (com.google.gson.JsonDeserializer<java.time.LocalTime>) (json, typeOfT, context) ->
            java.time.LocalTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_TIME))

        .registerTypeAdapter(java.time.LocalDateTime.class, (com.google.gson.JsonSerializer<java.time.LocalDateTime>) (src, typeOfSrc, context) ->
            new com.google.gson.JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        .registerTypeAdapter(java.time.LocalDateTime.class, (com.google.gson.JsonDeserializer<java.time.LocalDateTime>) (json, typeOfT, context) ->
            java.time.LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))

        .setPrettyPrinting()
        .create();

    /**
     * Serializa a lista de usuários fornecida e a grava no arquivo JSON configurado.
     * <p>
     * Caso ocorra qualquer exceção durante a escrita física do arquivo (como permissões de pasta),
     * o erro será capturado, impresso no console de erro padrão e a execução continuará de forma segura.
     * </p>
     *
     * @param itens A {@link ArrayList} contendo os objetos do tipo {@link Usuario} que devem ser salvos.
     */
    @Override
    public void salvar(ArrayList<Usuario> itens) {
        try {
            String json = gson.toJson(itens);
            Files.writeString(CAMINHO, json);
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lê o arquivo JSON físico e desserializa o seu conteúdo de volta para uma lista de usuários.
     * <p>
     * Se o arquivo de persistência ainda não existir (por ser a primeira execução do programa), 
     * o método retorna silenciosamente uma nova lista vazia. Caso ocorra uma falha crítica de leitura 
     * ou corrupção de dados, o erro é impresso no console e uma lista vazia é retornada para evitar 
     * o travamento do sistema.
     * </p>
     *
     * @return Uma {@link ArrayList} contendo os usuários recuperados do arquivo, ou uma lista vazia em caso de falhas ou arquivo inexistente.
     */
    @Override
    public ArrayList<Usuario> carregar() {
        try {
            if (!Files.exists(CAMINHO)) return new ArrayList<>();

            String json = Files.readString(CAMINHO);

            // Define o tipo genérico correto para o Gson reconstruir a lista tipada
            Type tipoLista = new TypeToken<ArrayList<Usuario>>(){}.getType();
            return gson.fromJson(json, tipoLista);

        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
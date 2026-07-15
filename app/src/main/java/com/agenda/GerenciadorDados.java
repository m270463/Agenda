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


public class GerenciadorDados implements Persistivel<Usuario> {

    private static final Path CAMINHO = Paths.get("usuarios.json");

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

    @Override
    public ArrayList<Usuario> carregar() {
        try {
            if (!Files.exists(CAMINHO)) return new ArrayList<>();

            String json = Files.readString(CAMINHO);

            Type tipoLista = new TypeToken<ArrayList<Usuario>>(){}.getType();
            return gson.fromJson(json, tipoLista);

        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
package com.agenda;



import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GerenciadorDados {

    private static final Path CAMINHO = Paths.get("usuarios.json");
    private static final Gson gson = new Gson(); // O "mágico" que faz a conversão

    public static void salvarUsuarios(ArrayList<Usuario> lista) {
        try {
            // O Gson transforma a lista toda em texto JSON automaticamente
            String json = gson.toJson(lista); 
            // Salva o texto no arquivo
            Files.writeString(CAMINHO, json); 
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> carregarUsuarios() {
        try {
            if (!Files.exists(CAMINHO)) return new ArrayList<>();

            String json = Files.readString(CAMINHO);
            
            Type tipoLista = new TypeToken<ArrayList<Usuario>>(){}.getType();
            return gson.fromJson(json, tipoLista);

        } catch (Exception e) {
            System.out.println("Erro ao carregar: " + e.getMessage());
            return new ArrayList<>();   
        }
    }
}
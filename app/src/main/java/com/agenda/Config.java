package com.agenda;

public class Config {
    public static String getEmail(){
        return App.dotenv.get("EMAIL");
    }

    public static String getSenha(){
        return App.dotenv.get("SENHA");
    }

}

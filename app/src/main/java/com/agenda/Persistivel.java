package com.agenda;

import java.util.ArrayList;

public interface Persistivel<T> {

    void salvar(ArrayList<T> itens);

    ArrayList<T> carregar();
}
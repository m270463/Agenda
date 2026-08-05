package com.agenda;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Interface genérica que define o contrato essencial para operações de persistência de dados.
 * <p>
 * Qualquer classe que necessite salvar e carregar coleções de objetos de forma permanente 
 * (seja em arquivos locais JSON/XML, bancos de dados, ou memória cache) deve implementar esta interface.
 * O uso do parâmetro genérico {@code <T>} garante o reaproveitamento de código para diferentes tipos de entidades.
 * </p>
 *
 * @param <T> O tipo de objeto ou entidade que será persistido e recuperado (ex: {@link Usuario}, {@link Evento}).
 * * @author Seu Nome
 * @version 1.0
 */
public interface Persistivel<T> {



    /**
     * Recupera (carrega) a coleção de itens previamente armazenada na mídia de persistência.
     * <p>
     * A implementação concreta deve ler o meio de armazenamento físico, desserializar os dados 
     * de volta para instâncias do tipo {@code T} e devolvê-los estruturados em uma lista. 
     * </p>
     * <p>
     * Recomenda-se fortemente que, caso o arquivo ou fonte de dados ainda não exista (como na primeira 
     * execução do sistema) ou ocorra um erro de leitura, o método trate a exceção internamente 
     * e retorne uma lista vazia ({@code new ArrayList<>()}) em vez de {@code null}, evitando falhas do 
     * tipo {@code NullPointerException} no restante do sistema.
     * </p>
     *
     * @return Uma {@link ArrayList} contendo todos os elementos do tipo {@code T} recuperados; 
     * ou uma lista vazia se não houver dados salvos ou se ocorrer uma falha na leitura.
     */
    ArrayList<T> carregar();

    LocalDate carregarUltimaVerificacao();

    void inserirUsuario(Usuario user);

    void editarUsuario(Usuario user);

    void inserirEvento(int idUser, Evento evento);

    void editarEvento(Evento evento, boolean mudouRepeticao);

    void removerEvento(Evento evento,boolean  mudouRepeticao);

    void inserirUltimaVerificacao(LocalDate ultimaVerificacao);
}
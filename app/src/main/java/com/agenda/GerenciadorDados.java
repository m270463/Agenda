package com.agenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;


public class GerenciadorDados implements Persistivel<Usuario> {
    private static final String URL_BANCO = "jdbc:sqlite:agenda.db";

    public GerenciadorDados() {
        criarTabelas();
    }


    private void criarTabelas() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100)," +
                "email VARCHAR(100)," +
                "telefone VARCHAR(20)," +
                "senha VARCHAR(100)" +
                ");";

        String sqlAgenda = "CREATE TABLE IF NOT EXISTS agenda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(150)," +
                "descricao TEXT," +
                "repeticao VARCHAR(50)," +
                "diaInicio VARCHAR(20)," +
                "horaInicio VARCHAR(10)," +
                "horaFim VARCHAR(10)," +
                "usuario_id INTEGER," +
                "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                ");";

        String sqlAgendaRepetitiva = "CREATE TABLE IF NOT EXISTS agendaRepetitiva (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(150)," +
                "descricao TEXT," +
                "repeticao VARCHAR(50)," +
                "diaInicio VARCHAR(20)," +
                "horaInicio VARCHAR(10)," +
                "horaFim VARCHAR(10)," +
                "usuario_id INTEGER," +
                "FOREIGN KEY (usuario_id) REFERENCES usuarios(id)" +
                ");";

        String sqlConfigurarcoes = "CREATE TABLE IF NOT EXISTS verificacoes (" +
                "ultimaVerificacao VARCHAR(20)" +
                ");";

        try (Connection conexao = DriverManager.getConnection(URL_BANCO);
             Statement stmt = conexao.createStatement()) {
            
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlAgenda);
            stmt.execute(sqlAgendaRepetitiva);
            stmt.execute(sqlConfigurarcoes);
            
            System.out.println("Banco de dados inicializado e tabelas verificadas com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }


    @Override
    public ArrayList<Usuario> carregar() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        
        try (Connection conexao = DriverManager.getConnection(URL_BANCO);
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")) { 
            while(rs.next()){
                Usuario user = new Usuario(rs.getString("nome"), rs.getString("email"), rs.getString("telefone"), rs.getString("senha"));
                user.setId(rs.getInt("id"));
                user.setAgenda(carregarEventosFixos(user.getId(), conexao));
                user.setAgendaRepetitiva(carregarEventosRepetitivos(user.getId(), conexao));
                listaUsuarios.add(user);
            }

        }catch(SQLException e){
            System.err.println("erro!");
        }

        return listaUsuarios;
    }

    public LocalDate carregarUltimaVerificacao(){
        LocalDate ultimaVerificacao = null;
        try(Connection conexao = DriverManager.getConnection(URL_BANCO);
            Statement stmt = conexao.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM verificacoes")) {
            while (rs.next()){
                String ultima = rs.getString("ultimaVerificacao");
                ultimaVerificacao = LocalDate.parse(ultima);
            }


            }catch(SQLException e){
            System.err.println("erro!");
        }    


        return ultimaVerificacao;
    }

    private HashMap<LocalDate,ArrayList<Evento>> carregarEventosFixos(int userId,Connection conexao){
        HashMap<LocalDate,ArrayList<Evento>> hashEventos = new HashMap<>();
        String sql = "SELECT * FROM agenda WHERE usuario_id = ?";
        try (PreparedStatement pstmt = conexao.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    String horaInicioStr = rs.getString("horaInicio");
                    String horaFimStr = rs.getString("horaFim");
                    LocalTime horaInicio  = null;
                    LocalTime horaFim = null;

                    if (horaInicioStr != null){
                        horaInicio = LocalTime.parse(horaInicioStr);
                        horaFim = LocalTime.parse(horaFimStr);
                    
                    }
                    Evento e = new Evento(rs.getString("nome"), rs.getString("descricao"), rs.getString("repeticao"), LocalDate.parse(rs.getString("diaInicio")), horaInicio, horaFim);
                    e.setId(rs.getInt("id"));
                    hashEventos.putIfAbsent(e.getDiaInicio(), new ArrayList<>());
                    hashEventos.get(e.getDiaInicio()).add(e);
                }
            }
        }  catch(SQLException e){
            e.printStackTrace();
        }
        return hashEventos;
    }
    
    private ArrayList<Evento> carregarEventosRepetitivos(int userId, Connection conexao){
        ArrayList<Evento> listaEventos = new ArrayList<>();
        String sql = "SELECT * FROM agendaRepetitiva WHERE usuario_id = ?";
                try (PreparedStatement pstmt = conexao.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            try(ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    String horaInicioStr = rs.getString("horaInicio");
                    String horaFimStr = rs.getString("horaFim");
                    LocalTime horaInicio  = null;
                    LocalTime horaFim = null;

                    if (horaInicioStr != null){
                        horaInicio = LocalTime.parse(horaInicioStr);
                        horaFim = LocalTime.parse(horaFimStr);
                    
                    }
                    Evento e = new Evento(rs.getString("nome"), rs.getString("descricao"), rs.getString("repeticao"), LocalDate.parse(rs.getString("diaInicio")), horaInicio, horaFim);
                    e.setId(rs.getInt("id"));
                    listaEventos.add(e);
                }
            }
        }  catch(SQLException e){
            e.printStackTrace();
        }


        return listaEventos;
    }

    public void inserirUltimaVerificacao(LocalDate ultimaVerificacao){
        String sql = "INSERT INTO verificacoes(ultimaVerificacao) VALUES (?)";
                

        try (Connection conexao = DriverManager.getConnection(URL_BANCO);
            PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, ultimaVerificacao.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário no banco: " + e.getMessage());
        }

    }


    public void inserirUsuario(Usuario usuario) {
        
        String sql = "INSERT INTO usuarios (nome, email, telefone, senha) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(URL_BANCO);
            PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getSenha());

            pstmt.executeUpdate();

            try (ResultSet chavesGeradas = pstmt.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    usuario.setId(chavesGeradas.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário no banco: " + e.getMessage());
        }
    }


    public void editarUsuario(Usuario usuario){
        String sql = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, senha = ? WHERE id = ?";
        try (Connection conexao = DriverManager.getConnection(URL_BANCO);
            PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getTelefone());
            pstmt.setString(4, usuario.getSenha());
            pstmt.setInt(5, usuario.getId());
            pstmt.executeUpdate();

            }catch(SQLException e){
                System.err.println(e.getMessage());
            }

    }


    

    public void inserirEvento(int userId, Evento evento){
        String sql = "";
        if (evento.getRepeticao().equals("Nunca")){
            sql = "INSERT INTO agenda (nome, descricao, repeticao, diaInicio, horaInicio, horaFim, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO agendaRepetitiva (nome, descricao, repeticao, diaInicio, horaInicio, horaFim, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        }
         try (Connection conexao = DriverManager.getConnection(URL_BANCO);
            PreparedStatement pstmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, evento.getNome());
            pstmt.setString(2, evento.getDescricao());
            pstmt.setString(3, evento.getRepeticao());
            pstmt.setString(4, evento.getDiaInicio().toString());
            if (evento.getHoraInicio() == null){
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            else{
                pstmt.setString(5, evento.getHoraInicio().toString());
                pstmt.setString(6, evento.getHoraFim().toString());
            }

            pstmt.setInt(7, userId);
            pstmt.executeUpdate();

            try (ResultSet chavesGeradas = pstmt.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    evento.setId(chavesGeradas.getInt(1));
                }
            }

            }catch(SQLException e){
                System.err.println(e.getMessage());
            }
    }

    public void editarEvento(Evento evento, boolean mudouRepeticao){
        if (!mudouRepeticao){
            String sql = "";
            if (evento.getRepeticao().equals("Nunca")) {
                sql = "UPDATE agenda SET nome = ?, descricao = ?, repeticao = ?, diaInicio = ?, horaInicio = ?, horaFim = ? WHERE id = ?";
            } else {
                sql = "UPDATE agendaRepetitiva SET nome = ?, descricao = ?, repeticao = ?, diaInicio = ?, horaInicio = ?, horaFim = ? WHERE id = ?";
            }

            try (Connection conexao = DriverManager.getConnection(URL_BANCO);
                PreparedStatement pstmt = conexao.prepareStatement(sql)) {
                pstmt.setString(1, evento.getNome());
                pstmt.setString(2, evento.getDescricao());
                pstmt.setString(3, evento.getRepeticao());
                pstmt.setString(4, evento.getDiaInicio().toString());
                if (evento.getHoraInicio() == null){
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                    pstmt.setNull(6, java.sql.Types.VARCHAR);
                }
                else{
                    pstmt.setString(5, evento.getHoraInicio().toString());
                    pstmt.setString(6, evento.getHoraFim().toString());
                }

                pstmt.setInt(7, evento.getId());
                pstmt.executeUpdate();

                }catch(SQLException e){
                    System.err.println(e.getMessage());
                }
        }
        else{
            removerEvento(evento, mudouRepeticao);
            inserirEvento(App.usuarioaAtivo.getId(), evento);
        }
    }

    public void removerEvento(Evento evento, boolean mudouRepeticao){
        String sql = "";
        if (!mudouRepeticao){
            if (evento.getRepeticao().equals("Nunca")) {
                sql = "DELETE FROM agenda WHERE id = ?";
            } else {
                sql = "DELETE FROM agendaRepetitiva WHERE id = ?";
            }

            try (Connection conexao = DriverManager.getConnection(URL_BANCO);
                PreparedStatement pstmt = conexao.prepareStatement(sql)) {

                pstmt.setInt(1, evento.getId());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao remover evento: " + e.getMessage());
            }
        }

        else{
            if (evento.getRepeticao().equals("Nunca")) {
                sql = "DELETE FROM agendaRepetitiva WHERE id = ?";
            } else {
                sql = "DELETE FROM agenda WHERE id = ?";
            }

            try (Connection conexao = DriverManager.getConnection(URL_BANCO);
                PreparedStatement pstmt = conexao.prepareStatement(sql)) {

                pstmt.setInt(1, evento.getId());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                System.err.println("Erro ao remover evento: " + e.getMessage());
            }
        }
    }



}
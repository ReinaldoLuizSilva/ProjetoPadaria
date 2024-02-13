package com.example.projetopadaria.database;

import com.example.projetopadaria.database.entidades.Pedido;
import com.example.projetopadaria.database.entidades.Produto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PedidoDAO {
    public static int inserirPedido(Pedido pedido) {
        int linhasAfetadas = 0;
        //inserindo o pedido do cliente
        try {
            String sql = "INSERT INTO pedido (nome_cliente, telefone_cliente, total_pedido, data_pedido) VALUES " +
                    "('" + pedido.getNome() + "', '" + pedido.getTelefone() + "', " + pedido.getTotal() + ", '" + pedido.getData() + "')";
            Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
            Statement stmt = conn.createStatement();
            linhasAfetadas = stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return linhasAfetadas;
    }

    public static List<Pedido> selecionarTudo() throws Exception {
        List<Pedido> pedidos = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pedido");
        while(rs.next()){
            int id = rs.getInt("idpedido");
            String nome = rs.getString("nome_cliente");
            String telefone = rs.getString("telefone_cliente");
            double total = rs.getDouble("total_pedido");
            Date dataSql = rs.getDate("data_pedido");
            LocalDate data = ((java.sql.Date) dataSql).toLocalDate();
            Pedido pedido = new Pedido(id, nome, telefone, total, data);
            pedidos.add(pedido);
        }
        return pedidos;
    }

    public static Pedido obterPorId(int idBusca) throws Exception {
        Pedido pedido = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pedido where idpedido = " + idBusca);
        while(rs.next()){
            int id = rs.getInt("idpedido");
            String nome = rs.getString("nome_cliente");
            String telefone = rs.getString("telefone_cliente");
            double total = rs.getDouble("total_pedido");
            Date dataSql = rs.getDate("data_pedido");
            LocalDate data = ((java.sql.Date) dataSql).toLocalDate();
            pedido = new Pedido(id, nome, telefone, total, data);
        }
        return pedido;
    }

    public static List<Pedido> consultaNome(String nomeBusca) throws Exception{
        List<Pedido> pedidos = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pedido WHERE nome_cliente LIKE '" + nomeBusca + "%'");


        while(rs.next()){
            int id = rs.getInt("idpedido");
            String nome = rs.getString("nome_cliente");
            String telefone = rs.getString("telefone_cliente");
            double valor = rs.getDouble("total_pedido");
            Date dataSql = rs.getDate("data_pedido");
            LocalDate data = ((java.sql.Date) dataSql).toLocalDate();
            Pedido pedido = new Pedido(id, nome, telefone, valor, data);
            pedidos.add(pedido);
        }
        return pedidos;
    }

    public static Pedido consultaPedidoRecente()throws Exception{
        Pedido pedido = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM pedido ORDER BY data_pedido DESC LIMIT 1");
        while (rs.next()){
            int id = rs.getInt("idpedido");
            String nome = rs.getString("nome_cliente");
            String telefone = rs.getString("telefone_cliente");
            double total = rs.getDouble("total_pedido");
            Date dataSql = rs.getDate("data_pedido");
            LocalDate data = ((java.sql.Date) dataSql).toLocalDate();
            pedido = new Pedido(id, nome, telefone, total, data);
        }
        return pedido;
    }

    public static int atualiza(Pedido pedido) throws Exception{
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        int linhasAfetadas = stmt.executeUpdate("UPDATE pedido SET nome_cliente = '" + pedido.getNome() + "', telefone_cliente = " +
                pedido.getTelefone() + ", total_pedido = " + pedido.getTotal() + " WHERE idpedido = " + pedido.getId());
        return linhasAfetadas;
    }

    public static int exclui(Pedido pedido) throws Exception{
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        int linhasAfetadas = stmt.executeUpdate("DELETE FROM pedido WHERE idpedido = " + pedido.getId());
        return linhasAfetadas;
    }
}

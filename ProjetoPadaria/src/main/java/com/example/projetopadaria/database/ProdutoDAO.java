package com.example.projetopadaria.database;

import com.example.projetopadaria.database.entidades.Produto;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public static int inserirProduto(Produto produto) throws Exception {
        java.util.Date data = produto.getValidade();
        java.sql.Date dataFormatada = new java.sql.Date(data.getTime());

        String sql = "INSERT INTO produto (nome_produto, preco_produto, validade_produto, quantidade_produto) VALUES (?, ?, ?, ?)";
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, produto.getNome());
        stmt.setDouble(2, produto.getPreco());
        stmt.setDate(3, dataFormatada);
        stmt.setInt(4, produto.getQuantidade());

        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas;
    }


    public static List<Produto> selecionarProdutos() throws Exception {
        List<Produto> produtos = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM produto");
        while (rs.next()){
            int id = rs.getInt("idproduto");
            String nome = rs.getString("nome_produto");
            double preco = rs.getDouble("preco_produto");
            Date validade = rs.getDate("validade_produto");
            int quantidade = rs.getInt("quantidade_produto");
            Produto produto = new Produto(id, nome,preco, validade, quantidade);
            produtos.add(produto);
        }
        return produtos;
    }

    public static Produto obterPorId(int idBusca) throws Exception{
        Produto produto = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM produto WHERE idproduto = " + idBusca);
        while(rs.next()){
            int id = rs.getInt("idproduto");
            String nome = rs.getString("nome_produto");
            double preco = rs.getDouble("preco_produto");
            Date validade = rs.getDate("validade_produto");
            int quantidade = rs.getInt("quantidade_produto");
            produto = new Produto(id, nome, preco, validade, quantidade);
        }
        return produto;
    }

    public static List<Produto> obterProdutosCaros() throws Exception{
        List<Produto> produtos = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM produto ORDER BY preco_produto DESC");
        while (rs.next()){
            int id = rs.getInt("idproduto");
            String nome = rs.getString("nome_produto");
            double preco = rs.getDouble("preco_produto");
            Date validade = rs.getDate("validade_produto");
            int quantidade = rs.getInt("quantidade_produto");
            Produto produto = new Produto(id, nome, preco, validade, quantidade);
            produtos.add(produto);
        }
        return produtos;
    }

    public static List<Produto> obterConsultaVencimento()throws Exception{
        List<Produto> produtos = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM produto ORDER BY validade_produto");
        while (rs.next()){
            int id = rs.getInt("idproduto");
            String nome = rs.getString("nome_produto");
            double preco = rs.getDouble("preco_produto");
            Date validade = rs.getDate("validade_produto");
            int quantidade = rs.getInt("quantidade_produto");
            Produto produto = new Produto(id, nome, preco, validade, quantidade);
            produtos.add(produto);
        }
        return produtos;
    }

    public static int exclui(Produto produto) throws Exception{
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        int linhasAfetadas = stmt.executeUpdate("DELETE FROM produto WHERE idproduto = " + produto.getId());
        return linhasAfetadas;
    }

    public static int atualiza(Produto produto) throws Exception {
        java.util.Date data = produto.getValidade();
        java.sql.Date dataFormatada = new java.sql.Date(data.getTime());
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();

        PreparedStatement stmt = conn.prepareStatement("UPDATE produto SET nome_produto = ?, preco_produto = ?, validade_produto = ?," +
                " quantidade_produto = ? WHERE idproduto = ?");
        stmt.setString(1, produto.getNome());
        stmt.setDouble(2, produto.getPreco());
        stmt.setDate(3, dataFormatada);
        stmt.setInt(4, produto.getQuantidade());
        stmt.setInt(5, produto.getId());

        int linhasAfetadas = stmt.executeUpdate();
        return linhasAfetadas;
    }

}
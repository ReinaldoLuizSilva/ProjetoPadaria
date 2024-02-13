package com.example.projetopadaria.database;

import com.example.projetopadaria.database.entidades.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public static int inserirFuncionario(Funcionario funcionario) {
        int linhasAfetadas = 0;
        try {
            String sql = "INSERT INTO funcionario (nome, cargo, salario, telefone) " +
                    "VALUES " + "('" + funcionario.getNome() + "', '" + funcionario.getCargo() +
                    "', " + funcionario.getSalario() + ", '" + funcionario.getTelefone() + "')";
            Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
            Statement stmt = conn.createStatement();
            linhasAfetadas = stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return linhasAfetadas;
    }

    public static List<Funcionario> selecionarTudo() throws Exception {
        List<Funcionario> funcionarios = new ArrayList<>();
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM funcionario");
        while (rs.next()){
            int id = rs.getInt("idfuncionario");
            String nome = rs.getString("nome");
            String cargo = rs.getString("cargo");
            double salario = rs.getDouble("salario");
            String telefone = rs.getString("telefone");
            Funcionario funcionario = new Funcionario(id, nome, cargo, salario, telefone);
            funcionarios.add(funcionario);
        }
        return funcionarios;
    }

    public static Funcionario obterPorId(int idBusca) throws Exception {
        Funcionario funcionario = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM funcionario where idfuncionario = " + idBusca);
        while(rs.next()){
            int id = rs.getInt("idfuncionario");
            String nome = rs.getString("nome");
            String cargo = rs.getString("cargo");
            double salario = rs.getDouble("salario");
            String telefone = rs.getString("telefone");
            funcionario = new Funcionario(id, nome, cargo, salario, telefone);
        }
        return funcionario;
    }

    public static int atualiza(Funcionario funcionario) throws Exception {
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        PreparedStatement pstmt = conn.prepareStatement("UPDATE funcionario SET nome = ?, cargo = ?," +
                " salario = ?, telefone = ? WHERE idfuncionario = ?");
        pstmt.setString(1, funcionario.getNome());
        pstmt.setString(2, funcionario.getCargo());
        pstmt.setDouble(3, funcionario.getSalario());
        pstmt.setString(4, funcionario.getTelefone());
        pstmt.setInt(5, funcionario.getIdFuncionario());

        int linhasAfetadas = pstmt.executeUpdate();
        pstmt.close();
        return linhasAfetadas;
    }

    public static int exclui(Funcionario funcionario) throws Exception{
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        int linhasAfetadas = stmt.executeUpdate("DELETE FROM funcionario WHERE idfuncionario = " + funcionario.getIdFuncionario());
        return linhasAfetadas;
    }

    public static Funcionario ganhaMais()throws Exception{
        Funcionario funcionario = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM funcionario ORDER BY salario DESC LIMIT 1");
        while (rs.next()){
            int id = rs.getInt("idfuncionario");
            String nome = rs.getString("nome");
            String cargo = rs.getString("cargo");
            String telefone = rs.getString("telefone");
            double salario = rs.getDouble("salario");
            funcionario = new Funcionario(id, nome, cargo, salario, telefone);
        }
        return funcionario;
    }

    public static Funcionario ganhaMenos()throws Exception{
        Funcionario funcionario = null;
        Connection conn = ConexaoDatabase.GetConexaoDatabase().gConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM funcionario ORDER BY salario LIMIT 1");
        while (rs.next()){
            int id = rs.getInt("idfuncionario");
            String nome = rs.getString("nome");
            String cargo = rs.getString("cargo");
            String telefone = rs.getString("telefone");
            double salario = rs.getDouble("salario");
            funcionario = new Funcionario(id, nome, cargo, salario, telefone);
        }
        return funcionario;
    }
}

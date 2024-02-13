package com.example.projetopadaria.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoDatabase {
    private Connection conn;
    private static ConexaoDatabase database;

    private ConexaoDatabase() throws Exception{

        try {
            String url = "jdbc:mysql://localhost:3306/Padaria?serverTimezone=UTC";
            String usuario = "root";
            String senha = "1234";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static ConexaoDatabase GetConexaoDatabase() throws Exception {
        if(database == null)
            database = new ConexaoDatabase();
        return database;
    }

    public Connection gConnection(){
        return conn;
    }
}

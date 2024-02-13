package com.example.projetopadaria.database.entidades;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Produto {

    private int id;
    private String nome;
    private double preco;
    private java.sql.Date validade;
    private int quantidade;

    public Produto(int id, String nome, double preco, java.sql.Date validade, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.validade = validade;
        this.quantidade = quantidade;
    }

    public Produto(String nome, double preco, java.sql.Date validade, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.validade = validade;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

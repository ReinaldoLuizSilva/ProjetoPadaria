package com.example.projetopadaria.database.entidades;

import java.time.LocalDate;
import java.util.Date;

public class Pedido {
    private int id;
    private String nome;
    private String telefone;
    private double total;
    private LocalDate data = LocalDate.now();

    public Pedido(int id, String nome, String telefone, double total, LocalDate data) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.total = total;
        this.data = data;
    }

    public Pedido(String nome, String telefone, double total) {
        this.nome = nome;
        this.telefone = telefone;
        this.total = total;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getData() {
        return data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

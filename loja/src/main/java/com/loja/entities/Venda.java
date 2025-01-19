package com.loja.entities;

import java.util.Date;
import java.util.List;

public class Venda {
    private Long id;
    private java.sql.Date dataVenda = new java.sql.Date(new Date().getTime()); //Gerar data e hora automaticamente
    private List<Item> itens;
    private Double totalVenda;

    public Venda() {}

    public Venda(List<Item> itens, Double totalVenda) {
        this.itens = itens;
        this.totalVenda = totalVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.sql.Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(java.sql.Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public Double getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(Double totalVenda) {
        this.totalVenda = totalVenda;
    }

  
    
}

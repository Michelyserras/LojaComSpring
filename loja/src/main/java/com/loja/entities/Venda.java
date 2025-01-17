package com.loja.entities;

import java.time.LocalDate;
import java.util.List;

public class Venda {
    private Long id;
    private LocalDate dataVenda;
    private List<Item> itens;
    private Double totalVenda;
    
    public Venda(LocalDate dataVenda, List<Item> itens, Double totalVenda) {
        this.dataVenda = dataVenda;
        this.itens = itens;
        this.totalVenda = totalVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
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

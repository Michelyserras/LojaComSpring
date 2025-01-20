package com.loja.entities.dto;

import com.loja.entities.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaDto {
    private Integer id;
    private java.sql.Date dataVenda;
    private List<Item> itens;
    private Double totalVenda;

    public VendaDto(Integer id, java.sql.Date dataVenda, List<Item> itens, Double totalVenda) {
        this.id = id;
        this.dataVenda = new java.sql.Date(new Date().getTime());
        this.itens = itens;
        this.totalVenda = totalVenda;
    }

    public void adicionarItemVenda(Item item) {
        this.itens.add(item);
    }

    public Integer getId() {
            return id;
        }

    public void setId(Integer id) {
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

package com.loja.entities.dto;

import com.loja.entities.Item;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VendaDto {
    private Integer id;
    private java.sql.Date dataVenda;
    private List<ItemDto> itensDto;
    private Double totalVenda;

    public VendaDto(Integer id, java.sql.Date dataVenda, List<ItemDto> itensDto, Double totalVenda) {
        this.id = id;
        this.dataVenda = new java.sql.Date(new Date().getTime());
        this.itensDto = itensDto;
        this.totalVenda = totalVenda;
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

    public List<ItemDto> getItens() {
            return itensDto;
        }

    public void setItens(List<ItemDto> itens) {
            this.itensDto = itens;
        }

    public Double getTotalVenda() {
            return totalVenda;
        }

    public void setTotalVenda(Double totalVenda) {
            this.totalVenda = totalVenda;
        }

}

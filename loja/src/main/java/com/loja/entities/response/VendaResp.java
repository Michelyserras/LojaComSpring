package com.loja.entities.response;

import java.util.List;
import java.util.stream.Collectors;

import com.loja.entities.Venda;

public class VendaResp {
    private int id;
    private String dataVenda;
    private List<ItemResp> itens;
    private double totalVenda;

    public VendaResp(Venda venda) {
        this.id = venda.getId();
        this.dataVenda = venda.getDataVenda().toString();
        this.totalVenda = venda.getTotalVenda();
        this.itens = venda.getItens().stream()
                          .map(ItemResp::new) // Mapeia para o DTO que não inclui venda_id
                          .collect(Collectors.toList());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(String dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemResp> getItens() {
        return itens;
    }

    public void setItens(List<ItemResp> itens) {
        this.itens = itens;
    }

    public double getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(double totalVenda) {
        this.totalVenda = totalVenda;
    }

}

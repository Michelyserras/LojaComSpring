package com.loja.entities.response;

import com.loja.entities.ItemVenda;

public class ItemResp {
    private int produto_id;
    private String nomeProduto;
    private int quantidade;
    private double valorUnitario;

    
    public ItemResp(ItemVenda item) {
        this.produto_id = item.getProduto_id();
        this.nomeProduto = item.getNomeProduto();
        this.quantidade = item.getQuantidade();
        this.valorUnitario = item.getValorUnitario();
    }
    
    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

}


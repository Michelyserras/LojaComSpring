package com.loja.entities;

public class ItemVenda {
    private int id;
    private int produto_id;
    private int venda_id;
    private int quantidade;
    private String nomeProduto;
    private Double valorUnitario;

    public ItemVenda(){}

    public ItemVenda(int produto_id, int quantidade, String nomeProduto, Double valorUnitario) {
        this.produto_id = produto_id;
        this.quantidade = quantidade;
        this.nomeProduto = nomeProduto;
        this.valorUnitario = valorUnitario;
        this.venda_id = 0;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produto_id;
    }

    public void setProdutoId(int produto) {
        this.produto_id = produto;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getVenda_id() {
        return venda_id;
    }

    public void setVenda_id(int venda_id) {
        this.venda_id = venda_id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}

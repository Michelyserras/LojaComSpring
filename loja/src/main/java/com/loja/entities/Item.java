package com.loja.entities;

public class Item {
    private int id;
    private int produto_id;
    private int venda_id;
    private int quantidade;

    public Item(){}

    public Item(int produto_id, int venda_id, int quantidade) {
        this.produto_id = produto_id;
        this.venda_id = venda_id;
        this.quantidade = quantidade;
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
}

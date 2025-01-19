package com.loja.entities;

public class Item {
    private int id;
    private int produto_id;
    private int quantidade;

    public Item(){}

    public Item(int produto_id, int quantidade) {
        this.produto_id = produto_id;
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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    
}

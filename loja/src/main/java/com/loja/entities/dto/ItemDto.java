package com.loja.entities.dto;

public class ItemDto {
    private int id;
    private int produto_id;
    private int venda_id;
    private int quantidade;
 
    
    public ItemDto(int id, int produto_id, int venda_id, int quantidade) {
        this.id = id;
        this.produto_id = produto_id;
        this.venda_id = venda_id;
        this.quantidade = quantidade;
    }

    public ItemDto(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produto_id;
    }

    public void setProdutoId(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

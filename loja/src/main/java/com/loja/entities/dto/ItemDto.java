package com.loja.entities.dto;

public class ItemDto {
    private int produto_id;
    private int quantidade;

    public ItemDto(){}

    public ItemDto(int produto_id, int quantidade){
        this.produto_id = produto_id;
        this.quantidade = quantidade;
    }

    public int getProdutoId() {
        return produto_id;
    }

    public void setProdutoId(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(int produto_id) {
        this.produto_id = produto_id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

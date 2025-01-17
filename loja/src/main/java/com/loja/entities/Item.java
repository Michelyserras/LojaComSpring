package com.loja.entities;

public class Item {
    private Long id;
    private Long produto_id;
    private Integer quantidade;
    private Double precoTotal;

    public Item(){}
    public Item(Long id, Long produto_id, Integer quantidade, Double precoTotal) {
        this.id = id;
        this.produto_id = produto_id;
        this.quantidade = quantidade;
        this.precoTotal = precoTotal;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produto_id;
    }

    public void setProdutoId(Long produto) {
        this.produto_id = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }


    
}

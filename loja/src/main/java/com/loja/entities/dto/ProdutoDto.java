package com.loja.entities.dto;

public class ProdutoDto {

    private String mensagem;
    private Long id;
    private String nome;
    private Double preco;
    private Integer quantidadeEstoque;
    private String descricao;

    public ProdutoDto(String mensagem){
        this.mensagem = mensagem;
    }

    public ProdutoDto(String mensagem, Long id, String nome, Double preco, Integer quantidadeEstoque, String descricao) {
        this.mensagem = mensagem;
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.descricao = descricao;
    }
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    
}

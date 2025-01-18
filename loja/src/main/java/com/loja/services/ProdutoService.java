package com.loja.services;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loja.dao.ProdutoDaoJDBC;
import com.loja.entities.Produto;

@Service
public class ProdutoService {

    @Autowired
    public ProdutoDaoJDBC repo;

    
    public Produto addProduto(String nome, Double preco, Integer quantidade, String descricao) throws SQLException {
        Produto novoProduto = null; // Inicializa a variável fora do bloco try
    
        try {
            // Validações dos campos
            if (nome.isEmpty() || preco == null || quantidade == null || descricao.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }
    
            if (quantidade < 0) {
                throw new IllegalArgumentException("Quantidade não pode ser negativa.");
            }
    
            if (preco < 0) {
                throw new IllegalArgumentException("Preço não pode ser negativo.");
            }
    
            if (repo.produtoExiste(nome)) {
                throw new IllegalArgumentException("Produto já existe no banco.");
            }
    
            // Cria um novo produto e chama o método do repositório
            Produto produto = new Produto(nome, preco, quantidade, descricao);
            novoProduto = repo.adicionarProduto(produto);
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto no banco: " + e.getMessage());
            throw e; // Relança a exceção para tratamento em outro nível, se necessário
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            throw e; // Relança a exceção para informar o chamador
        }
    
        return novoProduto;
    }
    



}

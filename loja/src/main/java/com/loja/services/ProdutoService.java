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

    public void addProduto(String nome, Double preco, Integer quantidade, String descricao ) throws SQLException{
       try {
            Produto pNovo = new Produto( nome, preco, quantidade, descricao);
            repo.adicionarProduto(pNovo);
        
       } catch (SQLException e) {
            System.err.println(e.getMessage());
       } 
    }

}

package com.loja.services;

import java.sql.SQLException;
import java.util.List;

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
          
          if(repo.produtoExiste(nome) == true){
               throw new IllegalArgumentException("Produto já existe.");
          }

            // Validações dos campos
          if (nome.isEmpty() || preco == null || quantidade == null || descricao.isEmpty()) {
               throw new IllegalArgumentException("Todos os campos são obrigatórios.");
          }
    
          if(quantidade <= 0) {
               throw new IllegalArgumentException("Quantidade tem que ser maior que zero.");
            }
    
          if(preco <= 0) {
                throw new IllegalArgumentException("O preço tem que ser maior que zero.");
          }
    
           // Cria um novo produto e chama o método do repositório
          Produto produto = new Produto(nome, preco, quantidade, descricao);
          novoProduto = repo.adicionarProduto(produto);

        } catch (SQLException e) {
            System.err.println("Erro ao adicionar produto no banco: " + e.getMessage());
            throw e; 
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de validação: " + e.getMessage());
            throw e;
        }
        return novoProduto;
    }
    
     public Produto removerProduto(int id) throws SQLException {
          try {
               Produto produtoExiste = repo.buscarProdutoPorId(id);
               if(produtoExiste == null) {
                    throw new IllegalArgumentException("Produto não encontrado.");
               }else{
                    repo.removerProduto(id);
                    return produtoExiste;
               }
          } catch (SQLException e) {
               System.err.println("Erro ao remover produto no banco: " + e.getMessage());
               throw e;
          }
     }

     public Produto buscarProduto(int id) throws SQLException {
          try {
               Produto produtoExiste = repo.buscarProdutoPorId(id);
               if(produtoExiste == null) {
                    throw new IllegalArgumentException("Produto não encontrado.");
               }
               return produtoExiste;
          } catch (SQLException e) {
               System.err.println("Erro ao buscar produto no banco: " + e.getMessage());
               throw e;
          }
     }

     public List<Produto> listarProdutos() throws SQLException {
          try {
               List<Produto> lista = repo.listarProdutos();
               if(lista.isEmpty()) {
                    throw new IllegalArgumentException("Não há produtos cadastrados.");
               }
               return lista;
          } catch (SQLException e) {
               System.err.println("Erro ao listar produtos no banco: " + e.getMessage());
               throw e;
          }
     }

     public Produto atualizarProduto(Produto produto) throws SQLException{
          try{
               Produto produtoEncontrado = repo.buscarProdutoPorId(produto.getId());
               if(produtoEncontrado != null){
                    repo.atualizarProduto(produto);
                    return produto;
               } else {
                    throw new IllegalArgumentException("Produto não encontrado.");
               }
          }catch(SQLException e){
               System.err.println("Erro ao atualizar produto no banco: " + e.getMessage());
               throw e;
          }
     }

     public boolean limparLista() throws SQLException {
          try {
               List<Produto> produtos = repo.listarProdutos();
               if(produtos.size() == 0){
                    throw new IllegalArgumentException("Não há produtos cadastrados.");
               }
               for(Produto p : produtos){
                    repo.removerProduto(p.getId());
               }
          } catch (SQLException e) {
               System.err.println("Erro ao remover todos os produtos no banco: " + e.getMessage());
               throw e;
          }
          return true;
     }

}

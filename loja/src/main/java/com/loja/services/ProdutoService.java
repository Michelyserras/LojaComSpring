package com.loja.services;

import java.sql.SQLException;
import java.util.List;

import com.loja.entities.dto.ProdutoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loja.dao.ProdutoDaoJDBC;
import com.loja.entities.Produto;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoDaoJDBC repo;


    public Produto addProduto(String nome, Double preco, Integer quantidade, String descricao) throws SQLException {
        Produto novoProduto = null;

        try {

            if (repo.produtoExiste(nome) == true) {
                throw new IllegalArgumentException("Produto já existe.");
            }

            // Validações dos campos
            if (nome.isEmpty() || preco == null || quantidade == null || descricao.isEmpty()) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }

            if (quantidade <= 0) {
                throw new IllegalArgumentException("Quantidade tem que ser maior que zero.");
            }

            if (preco <= 0) {
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
            if (produtoExiste == null) {
                return null;
            } else {
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

            if(id <= 0){
                throw new IllegalArgumentException("O ID deve ser maior que zero.");
            }

            Produto produtoExiste = repo.buscarProdutoPorId(id);
            if (produtoExiste == null) {
                throw new IllegalArgumentException("Produto com id: " + id + " não existe.");
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
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos no banco: " + e.getMessage());
            throw e;
        }
    }

    public Produto atualizarProduto(ProdutoDto produtoDto, int id) throws SQLException {
        try {
            Produto produtoEncontrado = repo.buscarProdutoPorId(id);

            if (produtoEncontrado != null) {
                Produto produtoAtualizado = new Produto(
                        produtoDto.getNome(),
                        produtoDto.getPreco(),
                        produtoDto.getQuantidadeEstoque() + produtoEncontrado.getQuantidadeEstoque(), //incrementa a quantidade no estoque
                        produtoDto.getDescricao()
                );
                produtoAtualizado.setId(id);
                repo.atualizarProduto(produtoAtualizado);
                return produtoAtualizado;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto no banco: " + e.getMessage());
            throw e;
        }
    }

    public boolean limparLista() throws SQLException {
        try {
            List<Produto> produtos = repo.listarProdutos();
            if (produtos.isEmpty()) {
                return false;
            }
            for (Produto p : produtos) {
                repo.removerProduto(p.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover todos os produtos no banco: " + e.getMessage());
            throw e;
        }
        return true;
    }

}

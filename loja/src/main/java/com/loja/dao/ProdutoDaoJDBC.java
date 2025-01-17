package com.loja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import com.loja.database.DB;
import com.loja.entities.Produto;


@Repository
public class ProdutoDaoJDBC implements ProdutoDao{

    private final Connection conn;

    public ProdutoDaoJDBC(Connection conn) {
        this.conn = conn;
    }


    @PostConstruct
    public void inicializar(){
        criarTabela();
    }

     public void criarTabela() {
        String query = """
            CREATE TABLE IF NOT EXISTS produtos (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                nome VARCHAR(255) NOT NULL,
                preco DECIMAL(10,2) NOT NULL,
                quantidade INT NOT NULL,
                descricao VARCHAR(255) NOT NULL
            )
        """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
            System.out.println("Tabela criada ou já existente.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    @Override
    public void adicionarProduto(Produto produto) throws SQLException {
        String query = "INSERT INTO produtos (nome, preco, quantidade, descricao) VALUES (?,?,?,?)";
        try(Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidadeEstoque());
            ps.setString(4, produto.getDescricao());
            ps.execute();
            System.out.println("Produto adicionado com sucesso!");
        }catch (SQLException e) {
            System.err.println("Erro ao adicionar produto." + e.getMessage());
        }
    }

    @Override
    public void removerProduto(Long id) {
       String query = "DELETE FROM produto WHERE ID = ?";
       try(Connection conn = DB.getConnection();
       PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(0, id);
            ps.execute();
            System.out.println("Produto removido com sucesso!");
            
       } catch (SQLException e) {
            System.err.println("Erro ao remover produto" + e.getMessage());
       }
    }

    @Override
    public void atualizarProduto(Produto produto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarproduto'");
    }

    @Override
    public List<Produto> listarProdutos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarItens'");
    }

}

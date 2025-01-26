package com.loja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;

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
    public Produto adicionarProduto(Produto produto) throws SQLException {
        String query = "INSERT INTO produtos (nome, preco, quantidade, descricao) VALUES (?,?,?,?)";
    
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
    
            // Define os valores dos parâmetros
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidadeEstoque());
            ps.setString(4, produto.getDescricao());
    
            // Executa a inserção
            int rowsAffected = ps.executeUpdate();
            System.out.println("Linhas afetadas: " + rowsAffected);
    
            // Recupera a chave gerada
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    produto.setId(generatedId); // Define o ID gerado na entidade
                    System.out.println("ID gerado: " + generatedId);
                } else {
                    System.err.println("Nenhuma chave foi gerada!");
                }
            }
    
            System.out.println("Produto adicionado com sucesso! ID: " + produto.getId());
        } catch (Error e) {
            System.err.println("Erro ao adicionar produto: " + e.getMessage());
            e.printStackTrace(); // Log do stack trace completo
        }
        return produto;
    }
    

    @Override
    public void removerProduto(int id) throws SQLException {
       String query = "DELETE FROM produtos WHERE ID = ?";
       try(Connection conn = DB.getConnection();
       PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.executeUpdate();
            System.out.println("Produto removido com sucesso!");
            
       } catch (SQLException e) {
            System.err.println("Erro ao remover produto" + e.getMessage());
       }
    }

    @Override
    public void atualizarProduto(Produto produto) throws SQLException {
        String query = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ?, descricao = ? WHERE id = ?";
        try(Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, produto.getNome());
            ps.setDouble(2, produto.getPreco());
            ps.setInt(3, produto.getQuantidadeEstoque());
            ps.setString(4, produto.getDescricao());
            ps.setLong(5, produto.getId());
            ps.execute();
            System.out.println("Produto atualizado com sucesso!");
        } catch(SQLException e) {
            System.err.println("Erro ao atualizar produto" + e.getMessage());
        }
    }

    @Override
    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String query = "SELECT * FROM produtos";
        try(Connection conn = DB.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQuantidadeEstoque(rs.getInt("quantidade"));
                produto.setDescricao(rs.getString("descricao"));
                produtos.add(produto);
            }
       
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos" + e.getMessage());
        }

        return produtos;
    }

    @Override
    public Produto buscarProdutoPorId(int id) throws SQLException {
        String query = "SELECT * FROM produtos WHERE id = ?";

        try(Connection conn = DB.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
                
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidade"));
                    produto.setDescricao(rs.getString("descricao"));
                    return produto;
                } else{
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por id" + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean produtoExiste(String nome) throws SQLException {
        List<Produto> produtos = listarProdutos();
        for(Produto p : produtos){
            if(p.getNome().equals(nome)){
                return true;
            }
        }
        return false;
    }
}

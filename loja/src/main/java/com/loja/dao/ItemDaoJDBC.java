package com.loja.dao;

import java.util.List;

import com.loja.database.DB;
import com.loja.entities.Item;

import java.sql.*;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;


@Repository
public class ItemDaoJDBC implements ItemDao{

    public ItemDaoJDBC(){
        criarTabela();
    }

    public void criarTabela() {
        String query = """
            CREATE TABLE IF NOT EXISTS loja.itens (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produto_id BIGINT NOT NULL,
                venda_id BIGINT,
                quantidade INT NOT NULL,
                preco_total DOUBLE NOT NULL
            )
        """;
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
            System.out.println("Tabela criada ou já existente.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    @Override
    public Item adicionarItem(Item item) throws SQLException {
        String query = "INSERT INTO itens (produto_id, venda_id quantidade) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getProdutoId());
            ps.setInt(2, item.getVenda_id());
            ps.setInt(3, item.getQuantidade());
            ps.execute();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getInt(1));
            }   

            System.out.println("Item adicionado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar item: " + e.getMessage());
        }
        return item;
    }

    @Override
    public void removerItem(Long id) throws SQLException {
        String query = "DELETE FROM itens WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ps.execute();
            System.out.println("Item removido com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao remover item: " + e.getMessage());
        }
    }

    @Override
    public List<Item> listarItens() throws SQLException {
        String query = "SELECT * FROM itens";
        List<Item> itens = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setProdutoId(rs.getInt("produto_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                itens.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens: " + e.getMessage());
        }
        return itens;
    }

    @Override
    public Item atualizarItem(Item item) throws SQLException{
        String query = "UPDATE itens SET produto_id = ?, quantidade = ?, preco_total = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);){
            ps.setLong(1, item.getProdutoId());
            ps.setInt(2, item.getQuantidade());
            ps.setLong(3, item.getId());
            ps.execute();
            System.out.println("Item atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item: " + e.getMessage());
        }
        return item;
    }
}

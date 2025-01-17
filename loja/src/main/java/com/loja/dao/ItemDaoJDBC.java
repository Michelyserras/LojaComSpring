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
    public void adicionarItem(Item item) {
        String query = "INSERT INTO itens (produto_id, quantidade, preco_total) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, item.getProdutoId());
            ps.setInt(2, item.getQuantidade());
            ps.setDouble(3, item.getPrecoTotal());
            ps.execute();
            System.out.println("Item adicionado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar item: " + e.getMessage());
        }
    }

    @Override
    public void removerItem(Long id) {
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
    public List<Item> listarItens() {
        String query = "SELECT * FROM itens";
        List<Item> itens = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong("id"));
                item.setProdutoId(rs.getLong("produto_id"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoTotal(rs.getDouble("preco_total"));
                itens.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens: " + e.getMessage());
        }
        return itens;
    }

    @Override
    public void atualizarItem(Item item) {
        throw new UnsupportedOperationException("Unimplemented method 'atualizarItem'");
    }
}

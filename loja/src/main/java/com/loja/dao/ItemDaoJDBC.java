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
            CREATE TABLE IF NOT EXISTS itens (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produto_id BIGINT NOT NULL,
                venda_id BIGINT NOT NULL,
                quantidade INT NOT NULL,
                FOREIGN KEY (produto_id) REFERENCES produtos(id),
                FOREIGN KEY (venda_id) REFERENCES vendas(id)
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
    public void adicionarItem(Integer vendaId, List<Item> itens) throws SQLException {
        String sql = "INSERT INTO itens (produto_id, venda_id, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Item item : itens) {
                ps.setLong(1, item.getProdutoId());
                ps.setLong(2, vendaId);
                ps.setInt(3, item.getQuantidade());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
            System.out.println("Item adicionado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar item: " + e.getMessage());
        }
    }

    @Override
    public void removerItem(Integer id) throws SQLException {
        String query = "DELETE FROM itens WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
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
                item.setVenda_id(rs.getInt("venda_id"));
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
        String query = "UPDATE itens SET produto_id = ?, venda_id = ?, quantidade = ?, preco_total = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);){
            ps.setInt(1, item.getProdutoId());
            ps.setInt(2, item.getVenda_id());
            ps.setInt(3, item.getQuantidade());
            ps.setInt(5, item.getId());
            ps.execute();
            System.out.println("Item atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar item: " + e.getMessage());
        }
        return item;
    }
}

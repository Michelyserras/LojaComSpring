package com.loja.dao;

import java.util.List;

import com.loja.database.DB;
import com.loja.entities.ItemVenda;

import java.sql.*;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;


@Repository
public class ItemDaoJDBC implements ItemDao{

    public ItemDaoJDBC(){
        criarTabela();
    }

    // ON DELETE CASCADE - Permite que quando uma venda for deletada, todos os itens associados a essa venda também serão deletados
    public void criarTabela() {
        String query = """
            CREATE TABLE IF NOT EXISTS itensVenda (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produto_id BIGINT NOT NULL,
                venda_id BIGINT NOT NULL,
                quantidade INT NOT NULL,
                FOREIGN KEY (produto_id) REFERENCES produtos(id),
                FOREIGN KEY (venda_id) REFERENCES vendas(id) ON DELETE CASCADE
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
    public ItemVenda adicionarItem(ItemVenda itemVenda) throws SQLException {
        String query = "INSERT INTO itensVenda (produto_id, venda_id, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, itemVenda.getProduto_id());
            ps.setInt(2, itemVenda.getVenda_id());
            ps.setInt(3, itemVenda.getQuantidade());

            int rowsAffected = ps.executeUpdate();
            System.out.println("Linhas afetadas: " + rowsAffected);


            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        itemVenda.setId(generatedId);
                        System.out.println("ID gerado: " + generatedId);
                    } else {
                        System.err.println("Nenhuma chave foi gerada!");
                    }
                }
            } else {
                System.err.println("Nenhuma linha foi afetada. A venda não foi inserida.");
            }

            System.out.println("ItemVenda adicionado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar itemVenda: " + e.getMessage());
        }

        return itemVenda;
    }

    @Override
    public void removerItem(Integer id) throws SQLException {
        String query = "DELETE FROM itensVenda WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.execute();
            System.out.println("ItemVenda removido com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao remover item: " + e.getMessage());
        }
    }

    @Override
    public List<ItemVenda> listarItens() throws SQLException {
        String query = "SELECT * FROM itensVenda";
        List<ItemVenda> itens = new ArrayList<>();
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setProdutoId(rs.getInt("produto_id"));
                itemVenda.setVenda_id(rs.getInt("venda_id"));
                itemVenda.setQuantidade(rs.getInt("quantidade"));
                itens.add(itemVenda);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar itens: " + e.getMessage());
        }
        return itens;
    }

    @Override
    public ItemVenda atualizarItem(ItemVenda itemVenda) throws SQLException{
        String query = "UPDATE itensVenda SET produto_id = ?, venda_id = ?, quantidade = ?, preco_total = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);){
            ps.setInt(1, itemVenda.getProdutoId());
            ps.setInt(2, itemVenda.getVenda_id());
            ps.setInt(3, itemVenda.getQuantidade());
            ps.setInt(5, itemVenda.getId());
            ps.execute();
            System.out.println("ItemVenda atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar itemVenda: " + e.getMessage());
        }
        return itemVenda;
    }
}

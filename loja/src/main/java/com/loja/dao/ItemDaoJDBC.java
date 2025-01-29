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

    // ON DELETE CASCADE - Quando uma venda é deletada, todos os itens vinculados a ela são deletados automaticamente
    // ON DELETE SET NULL - Quando um produto é deletado, seu id é desvinculado dos itensVenda
    public void criarTabela() {
        String query = """
            CREATE TABLE IF NOT EXISTS itensVenda (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                produto_id BIGINT,
                venda_id BIGINT NOT NULL,
                quantidade INT NOT NULL,
                nome_produto VARCHAR(255) NOT NULL,
                valor_unitario DECIMAL(10,2) NOT NULL,
                FOREIGN KEY (produto_id) REFERENCES produtos(id) ON DELETE SET NULL,
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
        String query = "INSERT INTO itensVenda (produto_id, venda_id, quantidade, nome_produto, valor_unitario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, itemVenda.getProduto_id());
            ps.setInt(2, itemVenda.getVenda_id());
            ps.setInt(3, itemVenda.getQuantidade());
            ps.setString(4, itemVenda.getNomeProduto());
            ps.setDouble(5, itemVenda.getValorUnitario());

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
        List<ItemVenda> itens = new ArrayList<>();

        String query = "SELECT * FROM itensVenda";

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
        String query = "UPDATE itensVenda SET quantidade = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);){
            ps.setInt(1, itemVenda.getQuantidade());
            ps.setInt(2, itemVenda.getId());
            ps.execute();
            System.out.println("ItemVenda atualizado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar itemVenda: " + e.getMessage());
        }
        return itemVenda;
    }

    @Override
    public void desvincularItemProduto(ItemVenda itemVenda) throws SQLException{
        String query = "UPDATE itensVenda SET produto_id = ? WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);){
            ps.setInt(1, itemVenda.getProdutoId());
            ps.setInt(2, itemVenda.getId());
            ps.execute();
            System.out.println("ItemVenda desvinculado de produto com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar itemVenda: " + e.getMessage());
        }
    }

    @Override
    public List<ItemVenda> buscarItemPorVenda(int id) throws SQLException {
        String query = "SELECT * FROM itensVenda WHERE venda_id = ?";
        List<ItemVenda> itens = new ArrayList<>();

        try(Connection conn = DB.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);){
                ps.setInt(1, id);
                try(ResultSet rs = ps.executeQuery()){

                    while(rs.next()){
                        ItemVenda item = new ItemVenda();
                        item.setId(rs.getInt("id"));
                        item.setProdutoId(rs.getInt("produto_id"));
                        item.setVenda_id(rs.getInt("venda_id"));
                        item.setQuantidade(rs.getInt("quantidade"));
                        item.setNomeProduto(rs.getString("nome_produto"));
                        item.setValorUnitario(rs.getDouble("valor_unitario"));

                        itens.add(item);

                    }

                }
                return itens;

        }catch(SQLException e){
            System.out.println("Erro ao buscar itemVenda: " + e.getMessage());
        }
        return null;
    }

    
}

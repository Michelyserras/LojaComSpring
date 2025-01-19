package com.loja.dao;

import com.loja.database.DB;
import com.loja.entities.Venda;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.sql.ResultSet;

/* COMENTÁRIO ÚTIL PARA VERIFICAR USO DE DATAS E LOCAL DATE
Não, o LocalDate não existe no SQL, mas é possível converter um LocalDate do Java para um Date do SQL.
Para converter um LocalDate do Java para um Date do SQL, pode-se usar o método estático valueOf() do sql.Date.
Para converter um Date do SQL para um LocalDate do Java, pode-se usar o método toLocalDate() do Date.
 */

@Repository
public class VendaDaoJDBC implements VendaDao{
    private final Connection conn;

    public VendaDaoJDBC(Connection conn) { this.conn = conn; }

    @PostConstruct
    public void inicializar() { }

    public void criarTabela() {
        String query = """
                CREATE TABLE IF NOT EXISTS vendas (
                    id BIGINT AUTO_INCREMENT PRIMERY KEY,
                    dataVenda DATE NOT NULL,
                    totalVenda DECIMAL(10,2) NOT NULL
                    )
                """;
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
            System.out.println("Tabela de vendas criada ou já existente");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela de vendas: " + e.getMessage());
        }
    }

    @Override
    public void adicionarVenda(Venda venda) throws SQLException {
        String query = "INSERT INTO vendas (dataVenda, totalVenda) VALUES (?, ?)";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, venda.getDataVenda());
            ps.setDouble(2, venda.getTotalVenda());
            ps.execute();
            System.out.println("Venda adicionada com sucesso");
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar a venda: " + e.getMessage());
        }
    }

    @Override
    public void removerVenda(Venda venda) throws SQLException {
        String query = "DELETE FROM vendas WHERE id=?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, venda.getId());
            ps.execute();
            System.out.println("Venda removida com sucesso");
        } catch (SQLException e) {
            System.err.println("Erro ao remover a venda: " + e.getMessage());
        }
    }

    @Override
    public void atualizarVenda(Venda venda) throws SQLException {
        String query = "UPDATE vendas SET dataVenda = ?, totalVenda = ? WHERE id=?";
        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, venda.getDataVenda());
            ps.setDouble(2, venda.getTotalVenda());
            ps.execute();
            System.out.println("Venda atualizada com sucesso");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar a venda: " + e.getMessage());
        }
    }

    @Override
    public List<Venda> listarVendas() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        String query = "SELECT * FROM vendas";

        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getLong("id"));
                venda.setDataVenda(rs.getDate("dataVenda"));
                venda.setTotalVenda(rs.getDouble("totalVenda"));
                vendas.add(venda);
            }
        } catch (SQLException e) {
            System.err.print("Erro ao listar as vendas: " + e.getMessage());
        }

        return vendas;
    }

    @Override
    public Venda buscarVendaPorId(Long id) throws SQLException {
        Venda venda = new Venda();
        String query = "SELECT * FROM vendas WHERE id=?";

        try (Connection conn = DB.getConnection();
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                venda.setId(rs.getLong("id"));
                venda.setDataVenda(rs.getDate("dataVenda"));
                venda.setTotalVenda(rs.getDouble("totalVenda"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a venda: " + e.getMessage());
        }

        return venda;
    }
}

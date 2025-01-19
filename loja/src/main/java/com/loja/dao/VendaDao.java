package com.loja.dao;

import com.loja.entities.Venda;

import java.sql.SQLException;
import java.util.List;

public interface VendaDao {
    void adicionarVenda(Venda venda) throws SQLException;
    void removerVenda(Venda venda) throws SQLException;
    void atualizarVenda(Venda venda) throws SQLException;
    List<Venda> listarVendas() throws SQLException;
    Venda buscarVendaPorId(Long id) throws SQLException;
}

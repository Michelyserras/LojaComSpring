package com.loja.dao;

import java.sql.SQLException;
import java.util.List;

import com.loja.entities.ItemVenda;

public interface ItemDao {
    public ItemVenda adicionarItem(ItemVenda itemVenda) throws SQLException;
    public void removerItem(Integer id) throws SQLException;
    public ItemVenda atualizarItem(ItemVenda itemVenda) throws SQLException;
    public List<ItemVenda> listarItens() throws SQLException;
    public void desvincularItemProduto(ItemVenda itemVenda) throws SQLException;
    public List<ItemVenda> buscarItemPorVenda(int id) throws SQLException;
}

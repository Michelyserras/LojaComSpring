package com.loja.dao;

import java.sql.SQLException;
import java.util.List;

import com.loja.entities.Item;

public interface ItemDao {
    public void adicionarItem(Item item) throws SQLException;
    public void removerItem(Integer id) throws SQLException;
    public Item atualizarItem(Item item) throws SQLException;
    public List<Item> listarItens() throws SQLException;
}

package com.loja.dao;

import java.sql.SQLException;
import java.util.List;

import com.loja.entities.Item;

public interface ItemDao {
    public Item adicionarItem(Item item) throws SQLException;
    public void removerItem(Long id);
    public Item atualizarItem(Item item);
    public List<Item> listarItens();
}

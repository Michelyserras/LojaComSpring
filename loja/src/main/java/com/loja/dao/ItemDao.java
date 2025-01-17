package com.loja.dao;

import java.sql.SQLException;
import java.util.List;

import com.loja.entities.Item;

public interface ItemDao {
    public void adicionarItem(Item item) throws SQLException;
    public void removerItem(Long id);
    public void atualizarItem(Item item);
    public List<Item> listarItens();

}

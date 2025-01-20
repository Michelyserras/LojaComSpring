package com.loja.services;

import com.loja.dao.ItemDaoJDBC;
import com.loja.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

public class ItemService {
    @Autowired
    private ItemDaoJDBC repo;

    public Item adicionarItemVenda(Item item) throws SQLException {
        Item novoItem = null;
        try {
            novoItem = repo.adicionarItem(item);
        } catch (SQLException e) {
             System.err.println("Erro ao adicionar item no banco: " + e.getMessage());
             throw e;
        }

        return novoItem;
    }
}

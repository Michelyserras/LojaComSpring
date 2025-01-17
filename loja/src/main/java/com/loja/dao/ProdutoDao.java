package com.loja.dao;
import java.sql.SQLException;
import java.util.List;
import com.loja.entities.Produto;

public interface ProdutoDao {
    public void adicionarProduto(Produto produto) throws SQLException;
    public void removerProduto(Long id);
    public void atualizarProduto(Produto Produto);
    public List<Produto> listarProdutos();
}

package com.loja.services;

import com.loja.dao.ItemDaoJDBC;
import com.loja.dao.ProdutoDaoJDBC;
import com.loja.entities.Item;
import com.loja.entities.Produto;
import com.loja.entities.dto.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;

public class ItemService {
    @Autowired
    private ItemDaoJDBC repo;
    @Autowired
    private ProdutoDaoJDBC repoProduto;

    public List<ItemDto> adicionarItem(int vendaId, List<Item> itens) throws SQLException {
        List<ItemDto> itensDto = null;

        try {
            for(Item item: itens){
                Produto produtoExiste = repoProduto.buscarProdutoPorId(item.getProduto_id());
                if(produtoExiste == null){
                    throw new IllegalArgumentException("O produto de ID informado não existe");
                }
                if(item.getQuantidade() <= 0){
                    throw new IllegalArgumentException("A quantidade do item precisa ser maior que 0");
                }
                if(item.getQuantidade() > produtoExiste.getQuantidadeEstoque()){
                    throw new IllegalArgumentException("Não há estoque suficiente");
                }

                produtoExiste.setQuantidadeEstoque(produtoExiste.getQuantidadeEstoque() - item.getQuantidade()); //Atualiza o estoque do produto

                ItemDto itemDto = new ItemDto(
                        item.getId(),
                        item.getProduto_id(),
                        vendaId,
                        item.getQuantidade()
                );
                itensDto.add(itemDto);
            }

            repo.adicionarItem(vendaId, itens);
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar o item no banco: " + e.getMessage());
        }

        return itensDto;
    }
}

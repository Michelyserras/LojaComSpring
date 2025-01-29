package com.loja.services;

import com.loja.dao.ItemDaoJDBC;
import com.loja.dao.ProdutoDaoJDBC;
import com.loja.dao.VendaDaoJDBC;
import com.loja.entities.ItemVenda;
import com.loja.entities.Produto;
import com.loja.entities.Venda;
import com.loja.entities.dto.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemDaoJDBC repo;
    @Autowired
    private ProdutoDaoJDBC repoProduto;
    @Autowired
    private VendaDaoJDBC repoVenda;
 

    public List<ItemVenda> adicionarItem(int vendaId, List<ItemVenda> itens) throws SQLException {
        List<ItemVenda> novosItens = new ArrayList<>();
    
        try {
            for (ItemVenda itemVenda : itens) {
                if (itemVenda == null || itemVenda.getProduto_id() <= 0 || itemVenda.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Um ou mais itens da lista estão inválidos. Verifique os campos obrigatórios.");
                }
    
                Produto produtoExiste = repoProduto.buscarProdutoPorId(itemVenda.getProduto_id());
    
                if (produtoExiste == null) {
                    throw new IllegalArgumentException("O produto de ID " + itemVenda.getProduto_id() + " não existe.");
                }
    
                // Atualiza os dados do itemVenda
                itemVenda.setVenda_id(vendaId);
                itemVenda.setNomeProduto(produtoExiste.getNome());
                itemVenda.setValorUnitario(produtoExiste.getPreco());
    
                // Adiciona o item na venda
                ItemVenda novoItemVenda = repo.adicionarItem(itemVenda);
                novosItens.add(novoItemVenda);
    
                // Atualiza o estoque do produto
                produtoExiste.setQuantidadeEstoque(produtoExiste.getQuantidadeEstoque() - itemVenda.getQuantidade());
                repoProduto.atualizarProduto(produtoExiste);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar os itens no banco: " + e.getMessage());
            throw e; // Relança a exceção para que possa ser tratada em outro nível
        }
    
        return novosItens;
    }
    

    public List<ItemVenda> atualizarItens(List<ItemDto> itensDto, Venda venda) throws SQLException {
        List<ItemVenda> itensAtualizados = new ArrayList<>();
        
        for (ItemDto itemDto : itensDto) {
            Produto produtoExistente = repoProduto.buscarProdutoPorId(itemDto.getProdutoId());
            if (produtoExistente == null)
                throw new IllegalArgumentException("Produto não encontrado");
            
            if (produtoExistente.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produtoExistente.getNome());
            }
        
            
            // Verifica se o item já existe na venda
            ItemVenda itemVendaExistente = venda.getItens().stream()
                    .filter(i -> i.getProduto_id() == itemDto.getProduto_id())
                    .findFirst()
                    .orElse(null);
    
            if (itemVendaExistente != null) {
                // Atualiza o item existente e incrementa a quantidade
                itemVendaExistente.setQuantidade(itemVendaExistente.getQuantidade() + itemDto.getQuantidade());
    
                produtoExistente.setQuantidadeEstoque(produtoExistente.getQuantidadeEstoque() - itemDto.getQuantidade());
                repoProduto.atualizarProduto(produtoExistente);
                repo.atualizarItem(itemVendaExistente);
    
                // Adiciona à lista de retorno
                itensAtualizados.add(itemVendaExistente);
            } else {
                // Cria um novo item
                ItemVenda novoItemVenda = new ItemVenda(
                        itemDto.getProduto_id(),
                        itemDto.getQuantidade(),
                        produtoExistente.getNome(),
                        produtoExistente.getPreco()
                );
                novoItemVenda.setVenda_id(venda.getId());
    
                repo.adicionarItem(novoItemVenda);
    
                // Adiciona à lista de retorno
                itensAtualizados.add(novoItemVenda);
            }
            
            totalVenda += itemVendaExistente.getQuantidade() * produtoExistente.getPreco();
            venda.setTotalVenda(totalVenda);
            repoVenda.atualizarVenda(venda);   
        }
        return itensAtualizados;
    }
    
}

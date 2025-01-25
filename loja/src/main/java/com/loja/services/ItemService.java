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

    public List<ItemVenda> adicionarItem(int vendaId, List<ItemVenda> itens) throws SQLException {
        List<ItemVenda> novosItens = new ArrayList<>();
        ItemVenda novoItemVenda = null;

        try {
            for(ItemVenda itemVenda : itens){
                Produto produtoExiste = repoProduto.buscarProdutoPorId(itemVenda.getProduto_id());

                if(produtoExiste == null){
                    throw new IllegalArgumentException("O produto de ID informado não existe");
                }

                if(itemVenda.getQuantidade() <= 0){
                    throw new IllegalArgumentException("A quantidade do itemVenda precisa ser maior que 0");
                }

                if(itemVenda.getQuantidade() > produtoExiste.getQuantidadeEstoque()){
                    throw new IllegalArgumentException("Não há estoque suficiente");
                }

                itemVenda.setVenda_id(vendaId); //Seto o id da venda correspondente
                itemVenda.setNomeProduto(produtoExiste.getNome());
                itemVenda.setValorUnitario(produtoExiste.getPreco());

                novoItemVenda = repo.adicionarItem(itemVenda); //Adiciono o itemVenda venda no banco de dados
                novosItens.add(novoItemVenda);

                produtoExiste.setQuantidadeEstoque(produtoExiste.getQuantidadeEstoque() - itemVenda.getQuantidade()); //Corrijo quantidade no estoque
                repoProduto.atualizarProduto(produtoExiste); //Atualizo essa quantidade no banco de dados
            }
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar o item no banco: " + e.getMessage());
        }

        return novosItens;
    }

    public List<ItemVenda> atualizarItens(List<ItemDto> itensDto, Venda venda) throws SQLException {
        List<ItemVenda> itens = new ArrayList<>();
        Double valorTotal = 0.0;

        for (ItemDto itemDto : itensDto) {
            Produto produtoExistente = repoProduto.buscarProdutoPorId(itemDto.getProdutoId());
            if (produtoExistente == null)
                throw new IllegalArgumentException("Produto não encontrado");

            // Verifica se o item já existe na venda
            ItemVenda itemVendaExistente = venda.getItens().stream()
                    .filter(i -> i.getProduto_id() == itemDto.getProduto_id())
                    .findFirst()
                    .orElse(null);

            if (itemVendaExistente != null) {
                // Atualiza o item existente e incrementa à quantidade

                itemVendaExistente.setQuantidade(itemVendaExistente.getQuantidade() + itemDto.getQuantidade());
                valorTotal += itemDto.getQuantidade() * produtoExistente.getPreco();

                //PRECISA ALTERAR O ESTOQUE DO PRODUTO AQUI

                repo.atualizarItem(itemVendaExistente);
            } else {
                // Cria um novo item

                ItemVenda novoItemVenda = new ItemVenda(
                        itemDto.getProduto_id(),
                        itemDto.getQuantidade(),
                        produtoExistente.getNome(),
                        produtoExistente.getPreco()
                );
                valorTotal += novoItemVenda.getQuantidade() * novoItemVenda.getValorUnitario();
                novoItemVenda.setVenda_id(venda.getId());

                itens.add(novoItemVenda);
                repo.adicionarItem(novoItemVenda);
            }
        }
        return itens;
    }
}

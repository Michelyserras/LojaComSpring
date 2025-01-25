package com.loja.services;

import com.loja.dao.ItemDaoJDBC;
import com.loja.dao.ProdutoDaoJDBC;
import com.loja.dao.VendaDaoJDBC;
import com.loja.entities.Item;
import com.loja.entities.Produto;
import com.loja.entities.Venda;
import com.loja.entities.dto.ItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {
    @Autowired
    private VendaDaoJDBC repo;
    @Autowired
    private ProdutoDaoJDBC repoProduto;

    public Venda adicionarVenda(List<ItemDto> itensDto) throws SQLException {
        Venda novaVenda = null;

        try {
            Double totalVenda = 0.0;
            Produto produtoExistente;
            List<Item> itens = new ArrayList<>();

            for(ItemDto itemDto: itensDto) { //Calcula valor total da venda e transforma DTO na Entidade Item
                produtoExistente = repoProduto.buscarProdutoPorId(itemDto.getProduto_id());
                totalVenda += itemDto.getQuantidade() * produtoExistente.getPreco();

                Item item = new Item(
                        itemDto.getProduto_id(),
                        itemDto.getQuantidade()
                );
                itens.add(item);
            }

            Venda venda = new Venda( //Instancia venda
                    itens,
                    totalVenda
            );

            novaVenda = repo.adicionarVenda(venda); //Adiciona a venda ao banco de dados
            System.out.println("Venda adicionada com sucesso." + novaVenda.getDataVenda() + " " + novaVenda.getId() + " " + novaVenda.getItens() + " " + novaVenda.getTotalVenda());
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar venda no banco: " + e.getMessage());
            throw e;
        }
         return novaVenda;
    }

    public Venda removerVenda(int id) throws  SQLException {
        try {
            Venda vendaExistente = repo.buscarVendaPorId(id);

            if(vendaExistente == null)
                throw new IllegalArgumentException("Venda não encontrada");
            else {
                repo.removerVenda(vendaExistente);
                return vendaExistente;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao remover a venda no banco: " + e.getMessage());
            throw e;
        }
    }

    public Venda buscarVenda(int id) throws SQLException {
        try {
            Venda vendaExistente = repo.buscarVendaPorId(id);
            if(vendaExistente == null)
                throw new IllegalArgumentException("Venda não encontrada");
S
            return vendaExistente;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda no banco: " + e.getMessage());
            throw e;
        }
    }

    public List<Venda> listarVendas() throws SQLException {
        try {
            List<Venda> lista = repo.listarVendas();
            if(lista.isEmpty())
                throw new IllegalArgumentException("Não há vendas cadastradas");
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas no banco: " + e.getMessage());
            throw e;
        }
    }

    public Venda atualizarVenda(Venda venda) throws SQLException {
        try {
            Venda vendaExistente = repo.buscarVendaPorId(venda.getId());
            if(vendaExistente == null)
                throw new IllegalArgumentException("Venda não encontrada");
            else {
                repo.atualizarVenda(venda);
                return venda;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar venda no banco: " + e.getMessage());
            throw  e;
        }
    }

    public boolean limparListaDeVendas() throws SQLException {
        try {
            List<Venda> lista = repo.listarVendas();
            if(lista.isEmpty())
                throw new IllegalArgumentException("Não há vendas cadastradas");
            for(Venda v: lista)
                repo.removerVenda(v);
        } catch (SQLException e) {
             System.err.println("Erro ao remover todas as vendas no banco: " + e.getMessage());
             throw e;
        }
        return true;
    }
}

package com.loja.services;

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
            List<ItemVenda> itens = new ArrayList<>();

            for(ItemDto itemDto: itensDto) { //Calcula valor total da venda e transforma DTO na Entidade ItemVenda
                produtoExistente = repoProduto.buscarProdutoPorId(itemDto.getProduto_id());
                totalVenda += itemDto.getQuantidade() * produtoExistente.getPreco();

                ItemVenda itemVenda = new ItemVenda(
                        itemDto.getProduto_id(),
                        itemDto.getQuantidade()
                );
                itens.add(itemVenda);
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

    public Venda removerVenda(Venda venda) throws  SQLException {
        try {
            repo.removerVenda(venda);
            return venda;
        } catch (SQLException e) {
            System.err.println("Erro ao remover a venda no banco: " + e.getMessage());
            throw e;
        }
    }

    public Venda buscarVenda(int id) throws SQLException {
        try {
            Venda vendaExistente = repo.buscarVendaPorId(id);
            if(vendaExistente == null)
                return null;

            return vendaExistente;
        } catch (SQLException e) {
            System.err.println("Erro ao buscar venda no banco: " + e.getMessage());
            throw e;
        }
    }

    public List<Venda> listarVendas() throws SQLException {
        try {
            List<Venda> lista = repo.listarVendas();
            return lista;
        } catch (SQLException e) {
            System.err.println("Erro ao listar vendas no banco: " + e.getMessage());
            throw e;
        }
    }

    public Venda atualizarVenda(List<ItemVenda> itens, int id) throws SQLException {
        try {
            Double valorTotal = 0.0;
            for(ItemVenda i : itens){
                Produto produto = repoProduto.buscarProdutoPorId(i.getProdutoId());
                valorTotal += i.getQuantidade() * produto.getPreco();
            }

            Venda vendaAtualizada = new Venda(
                    itens,
                    valorTotal
            );

            vendaAtualizada.setId(id);
            repo.atualizarVenda(vendaAtualizada);
            return vendaAtualizada;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar venda no banco: " + e.getMessage());
            throw  e;
        }
    }

    public boolean limparListaDeVendas() throws SQLException {
        try {
            List<Venda> lista = repo.listarVendas();
            if(lista.isEmpty())
                return false;
            else{
                for(Venda v: lista)
                    repo.removerVenda(v);
            }
        } catch (SQLException e) {
             System.err.println("Erro ao remover todas as vendas no banco: " + e.getMessage());
             throw e;
        }
        return true;
    }
}

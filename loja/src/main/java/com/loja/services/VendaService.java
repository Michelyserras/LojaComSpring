package com.loja.services;

import com.loja.dao.ItemDaoJDBC;
import com.loja.dao.VendaDaoJDBC;
import com.loja.entities.Item;
import com.loja.entities.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class VendaService {
    @Autowired
    private VendaDaoJDBC repo;
    @Autowired
    private ItemDaoJDBC repoItem;

    public Venda adicionarVenda(Venda venda) throws SQLException {
        Venda novaVenda = null;
        try {
            try {
                if(venda.getItens().isEmpty())
                    throw new IllegalArgumentException("Não é possível adicionar uma venda sem nenhum item");

                for(Item i: venda.getItens()) {
                    i.setVenda_id(venda.getId()); //Atribui automaticamente o id da venda que o item está ligado
                    repoItem.adicionarItem(i);//Adiciona os itens da lista da Venda no banco de dados na tabela Item
                }
                System.out.println("Todos os itens da lista foram adicionados com sucesso");
            } catch (SQLException e) {
                System.err.println("Erro ao adicionar o item no banco: " + e.getMessage());
                throw e;
            }
            novaVenda = repo.adicionarVenda(venda); //Adiciona a venda ao banco de dados
            System.out.println("Venda adicionada com sucesso.");
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

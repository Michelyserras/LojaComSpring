package com.loja.controllers;

import com.loja.entities.ItemVenda;
import com.loja.entities.Venda;
import com.loja.entities.dto.VendaDto;
import com.loja.services.ItemService;
import com.loja.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired
    private VendaService service;
    @Autowired
    private ItemService serviceItem;

    @PostMapping("/add")
    public ResponseEntity<?> adicionarVenda(@RequestBody VendaDto vendaDto) {
        try {
            if(vendaDto.getItensDto().isEmpty())
                throw new IllegalArgumentException("A lista de itens na venda não pode ser vazia");

            Venda novaVenda = service.adicionarVenda(vendaDto.getItensDto());
            List<ItemVenda> itens = serviceItem.adicionarItem(novaVenda.getId(), novaVenda.getItens());

            Map<String, Object> response = new HashMap<>();
            response.put("Venda cadastrada com sucesso!", novaVenda);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarVendaPorId(@RequestParam int id) {
        try {
            Venda venda = service.buscarVenda(id);
            if(venda == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }

            Map<String, Object> reponse = new HashMap<>();
            reponse.put("Venda encontrada: ", venda);

            return ResponseEntity.status(HttpStatus.OK).body(reponse);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listarVendas() {
        try {
            List<Venda> vendas = service.listarVendas();
            if(vendas.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há vendas cadastradas.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Vendas encontradas: ", vendas);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover")
    public ResponseEntity<?> removerVenda(@RequestParam int id) {
        try {
            Venda venda = service.buscarVenda(id);
            if(venda == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }

            Venda vendaRemovida = service.removerVenda(venda);

            Map<String, Object> response = new HashMap<>();
            response.put("Venda excluida: ", vendaRemovida);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarVenda(@RequestParam int id, @RequestBody VendaDto vendaDto) {
        try {
            if(vendaDto == null || vendaDto.getItensDto().isEmpty())
                throw new IllegalArgumentException("É necessário atualizar pelo menos 1 campo da venda");

            Venda vendaExiste = service.buscarVenda(id);
            if(vendaExiste == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }

            List<ItemVenda> itens = serviceItem.atualizarItens(vendaDto.getItensDto(), vendaExiste);
            Venda vendaAtualizada = service.atualizarVenda(itens, id);

            Map<String, Object> response = new HashMap<>();
            response.put("Venda atualizada com sucesso!", vendaAtualizada);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @DeleteMapping("/todos")
    public ResponseEntity<?> limparLista() {
        try {
            boolean listaVazia = service.limparListaDeVendas();
            if(listaVazia) {
                return ResponseEntity.status(HttpStatus.OK).body("Lista de vendas foi esvaziada.");
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há vendas cadastradas");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }
}

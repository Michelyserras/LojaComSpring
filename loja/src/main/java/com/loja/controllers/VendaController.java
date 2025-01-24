package com.loja.controllers;

import com.loja.entities.Item;
import com.loja.entities.Venda;
import com.loja.entities.dto.ItemDto;
import com.loja.entities.dto.VendaDto;
import com.loja.services.ItemService;
import com.loja.services.ProdutoService;
import com.loja.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
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
    public ResponseEntity<?> adicionarVenda(@RequestBody Venda venda) {
        try {
            Venda novaVenda = service.adicionarVenda(venda.getItens());
            List<Item> itens = serviceItem.adicionarItem(novaVenda.getId(), novaVenda.getItens());
            List<ItemDto> itensDto = new ArrayList<>();

            for(Item i: itens){
                ItemDto itemDto = new ItemDto(
                        i.getId(),
                        i.getProduto_id(),
                        i.getVenda_id(),
                        i.getQuantidade()
                );
                itensDto.add(itemDto);
            }

            VendaDto vendaDto = new VendaDto(
                    novaVenda.getId(),
                    novaVenda.getDataVenda(),
                    itensDto,
                    novaVenda.getTotalVenda()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("Venda cadastrada com sucesso!", vendaDto);

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
    public ResponseEntity<?> buscarVendaPorId(@RequestBody int id) {
        try {
            Venda venda = service.buscarVenda(id);
            if(venda == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }
            List<ItemDto> itensDto = new ArrayList<>();
            for(Item i : venda.getItens()){
                ItemDto itemDto = new ItemDto();
                itemDto.setId(i.getId());
                itemDto.setProdutoId(i.getProdutoId());
                itemDto.setVenda_id(i.getVenda_id());
                itemDto.setQuantidade(i.getQuantidade());
                itensDto.add(itemDto);
            }

            VendaDto vendaDto = new VendaDto(
                    venda.getId(),
                    venda.getDataVenda(),
                    itensDto,
                    venda.getTotalVenda()
            );
            Map<String, Object> reponse = new HashMap<>();
            reponse.put("Venda encontrada: ", vendaDto);

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

            List<VendaDto> vendasDto = new ArrayList<>();
            for(Venda venda : vendas) {
                List<ItemDto> itensDto = new ArrayList<>();
                for(Item i : venda.getItens()){
                    ItemDto itemDto = new ItemDto();
                    itemDto.setId(i.getId());
                    itemDto.setProdutoId(i.getProdutoId());
                    itemDto.setVenda_id(i.getVenda_id());
                    itemDto.setQuantidade(i.getQuantidade());
                    itensDto.add(itemDto);
                }

                VendaDto vendaDto = new VendaDto(
                        venda.getId(),
                        venda.getDataVenda(),
                        itensDto,
                        venda.getTotalVenda()
                );
                vendasDto.add(vendaDto);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("Vendas encontradas: ", vendasDto);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover")
    public ResponseEntity<?> removerVenda(@RequestParam int id) {
        try {
            Venda vendaRemovida = service.removerVenda(id);
            if(vendaRemovida == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Venda excluida: ", vendaRemovida);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarVenda(@RequestBody Venda venda) {
        try {
            Venda vendaExiste = service.buscarVenda(venda.getId());

            if(vendaExiste == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada.");
            }

            Venda vendaAtualizada = service.atualizarVenda(venda);

            List<ItemDto> itensDto = new ArrayList<>();
            for(Item i : vendaAtualizada.getItens()){
                ItemDto itemDto = new ItemDto();
                itemDto.setId(i.getId());
                itemDto.setProdutoId(i.getProdutoId());
                itemDto.setVenda_id(i.getVenda_id());
                itemDto.setQuantidade(i.getQuantidade());
                itensDto.add(itemDto);
            }

            VendaDto vendaDto = new VendaDto(
                    vendaAtualizada.getId(),
                    vendaAtualizada.getDataVenda(),
                    itensDto,
                    vendaAtualizada.getTotalVenda()
            );
            Map<String, Object> response = new HashMap<>();
            response.put("Venda atualizada com sucesso!", vendaDto);

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
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível excluir os produtos.");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        }
    }
}

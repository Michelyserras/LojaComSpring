package com.loja.controllers;

import com.loja.entities.Venda;
import com.loja.entities.dto.VendaDto;
import com.loja.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/vendas")
public class VendaController {
    @Autowired
    private VendaService service;

    @PostMapping("/add")
    public ResponseEntity<?> adicionarVenda(@RequestBody Venda venda) {
        try {
            Venda novaVenda = service.adicionarVenda(venda);

            VendaDto vendaDto = new VendaDto(
                    novaVenda.getId(),
                    novaVenda.getDataVenda(),
                    novaVenda.getItens(),
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
}

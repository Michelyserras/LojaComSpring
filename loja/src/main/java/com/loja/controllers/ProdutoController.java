package com.loja.controllers;

import java.sql.SQLException;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.loja.entities.Produto;
import com.loja.entities.dto.ProdutoDto;
import com.loja.services.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto", description = "API para gerenciamento de produtos")
public class ProdutoController {

    @Autowired
    public ProdutoService service;

    @PostMapping("/add")
    @Operation(summary = "Adicionar Produto", description = "Cadastra um novo produto no sistema")
    public ResponseEntity<?> adicionarProduto(@RequestBody ProdutoDto produtoDto) {
        try {
            Produto novoProduto = service.addProduto(produtoDto.getNome(), produtoDto.getPreco(), produtoDto.getQuantidadeEstoque(), produtoDto.getDescricao());

            Map<String, Object> response = new HashMap<>();
            response.put("Produto cadastrado com sucesso!", novoProduto);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar Produto por ID", description = "Retorna um produto pelo seu ID")
    public ResponseEntity<?> buscarProdutoPorId(@RequestParam int id) {
        try {
            Produto produto = service.buscarProduto(id);
            if (produto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Produto encontrado:", produto);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar Produtos", description = "Retorna uma lista de todos os produtos cadastrados")
    public ResponseEntity<?> listarProdutos() {
        try {
            List<Produto> produtos = service.listarProdutos();
            if (produtos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há produtos cadastrados.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Produtos Encontrados:", produtos);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/remover")
    @Operation(summary = "Remover Produto", description = "Exclui um produto pelo seu ID")
    public ResponseEntity<?> removerProduto(@RequestParam int id) {
        try {
            Produto produtoRemovido = service.removerProduto(id);
            if (produtoRemovido == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("Produto Excluído:", produtoRemovido);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @PutMapping("/atualizar")
    @Operation(summary = "Atualizar Produto", description = "Atualiza um produto existente pelo ID")
    public ResponseEntity<?> atualizarProduto(@RequestParam int id, @RequestBody ProdutoDto produtoDto) {
        try {
            Produto produtoExiste = service.buscarProduto(id);
            if (produtoExiste == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
            }

            Produto produtoAtualizado = service.atualizarProduto(produtoDto, id);

            Map<String, Object> response = new HashMap<>();
            response.put("Produto atualizado com sucesso!", produtoAtualizado);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }

    @DeleteMapping("/todos")
    @Operation(summary = "Remover Todos os Produtos", description = "Remove todos os produtos cadastrados")
    public ResponseEntity<?> limparLista() {
        try {
            boolean listaVazia = service.limparLista();
            if (listaVazia) {
                return ResponseEntity.status(HttpStatus.OK).body("Lista de produtos foi esvaziada.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível excluir os produtos.");
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
        }
    }
}

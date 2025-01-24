package com.loja.controllers;

import java.sql.SQLException;

import java.util.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.loja.entities.Produto;
import com.loja.entities.dto.ProdutoDto;

import com.loja.services.ProdutoService;








@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    
    @Autowired
    public ProdutoService service;

   @PostMapping("/add")
   public ResponseEntity<?> adicionarProduto(@RequestBody Produto produto) {
              try{
                     Produto novoProduto = service.addProduto(produto.getNome(), produto.getPreco(), produto.getQuantidadeEstoque(), produto.getDescricao());

                     ProdutoDto produtoDto = new ProdutoDto(
                            novoProduto.getId(),
                            novoProduto.getNome(), 
                            novoProduto.getPreco(), 
                            novoProduto.getQuantidadeEstoque(), 
                            novoProduto.getDescricao()
                            );

                     Map<String, Object> response = new HashMap<>(); // HashMap para armazenar a resposta
                     response.put("Produto cadastrado com sucesso!", produtoDto);

                     return ResponseEntity.status(HttpStatus.CREATED).body(response);

              }catch (IllegalArgumentException e) {
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
              } catch (SQLException e){
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }
       }
   
       @GetMapping("/buscar")
       public ResponseEntity<?> buscarProdutoPorId(@RequestParam int id) {
              try {
                     Produto produto = service.buscarProduto(id);
                     if(produto == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
                     }
                     ProdutoDto produtoDto = new ProdutoDto(
                            produto.getId(),
                            produto.getNome(), 
                            produto.getPreco(), 
                            produto.getQuantidadeEstoque(), 
                            produto.getDescricao()
                            );
                     Map<String, Object> response = new HashMap<>();
                     response.put("Produto encontrado: ", produtoDto);

                     return ResponseEntity.status(HttpStatus.OK).body(response);

              } catch (SQLException e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }
       }
    
       @GetMapping("/listar")
       public ResponseEntity<?> listarProdutos() {
              try {
                     List<Produto> produtos = service.listarProdutos();
                     if(produtos.isEmpty()) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há produtos cadastrados.");
                     }
                     List<ProdutoDto> produtosDto = new ArrayList<>();
                     for(Produto produto : produtos) {
                            ProdutoDto produtoDto = new ProdutoDto(
                                   produto.getId(),
                                   produto.getNome(), 
                                   produto.getPreco(), 
                                   produto.getQuantidadeEstoque(), 
                                   produto.getDescricao()
                                   );
                            produtosDto.add(produtoDto);
                     }


                     Map<String, Object> response = new HashMap<>();
                     response.put("Produtos Encontrados:", produtosDto);

                     return ResponseEntity.status(HttpStatus.OK).body(response);
              } catch (SQLException e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }
       }

       @DeleteMapping("/remover")
       public ResponseEntity<?> removerProduto(@RequestParam int id) {
              try {
                     Produto produtoRemovido = service.removerProduto(id);
                     if(produtoRemovido == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
                     }

                     Map<String, Object> response = new HashMap<>();
                     response.put("Produtos Excluido:", produtoRemovido);
                     return ResponseEntity.status(HttpStatus.OK).body(response);
              } catch (SQLException e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }

       }


       @PutMapping("/atualizar")
       public ResponseEntity<?> atualizarProduto(@RequestBody Produto produto) {
              try {
                     Produto produtoExiste = service.buscarProduto(produto.getId());
                     
                     if(produtoExiste == null) {
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
                     }

                     Produto produtoAtualizado = service.atualizarProduto(produto);

                     ProdutoDto produtoDto = new ProdutoDto(
                            produtoAtualizado.getId(),
                            produtoAtualizado.getNome(), 
                            produtoAtualizado.getPreco(), 
                            produtoAtualizado.getQuantidadeEstoque(), 
                            produtoAtualizado.getDescricao()
                            );
                     Map<String, Object> response = new HashMap<>();
                     response.put("Produto atualizado com sucesso!", produtoDto);

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
       public ResponseEntity<?> limparLista() {
              try {
                     boolean listaVazia = service.limparLista();
                     if(listaVazia) {
                            return ResponseEntity.status(HttpStatus.OK).body("Lista de produtos foi esvaziada.");
                     }else{
                             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível excluir os produtos.");
                     }
              } catch (SQLException e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }
       }


}

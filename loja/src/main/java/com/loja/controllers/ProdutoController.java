package com.loja.controllers;

import java.sql.SQLException;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loja.entities.Produto;
import com.loja.entities.dto.ProdutoDto;

import com.loja.services.ProdutoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
                     return ResponseEntity.status(HttpStatus.CREATED).body("Produto cadastrado: " + produtoDto);
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
                     return ResponseEntity.status(HttpStatus.OK).body("Produto encontrado: " + produtoDto);
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
                     return ResponseEntity.status(HttpStatus.OK).body("Produtos encontrados: "+ produtosDto);
              } catch (SQLException e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no banco de dados: " + e.getMessage());
              } catch (Exception e) {
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
              }
       }



}

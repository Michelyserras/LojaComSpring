package com.loja.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
   public ResponseEntity<ProdutoDto> postMethodName(@RequestBody Produto produto) {
       try{
              Produto novoProduto = service.addProduto(produto.getNome(), produto.getPreco(), produto.getQuantidadeEstoque(), produto.getDescricao());
              
              System.out.println(" Controller = ID do produto após inserção: " + novoProduto.getId());

              ProdutoDto produtoDto = new ProdutoDto(
                     "Produto cadastrado com sucesso",
                     novoProduto.getId(),
                     novoProduto.getNome(), 
                     novoProduto.getPreco(), 
                     novoProduto.getQuantidadeEstoque(), 
                     novoProduto.getDescricao()
                     );
              return ResponseEntity.status(HttpStatus.CREATED).body(produtoDto);
       }catch(Exception e){
              ProdutoDto produtoDtoErro = null;
              produtoDtoErro = new ProdutoDto("Erro ao cadastrar produto");
              return ResponseEntity.badRequest().body(produtoDtoErro);
       }
   }
   
    
}

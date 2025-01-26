package com.loja.entities.dto;
import java.util.ArrayList;
import java.util.List;

public class VendaDto {
    private List<ItemDto> itensDto;

    public VendaDto(){
        this.itensDto = new ArrayList<>();
    }

    public List<ItemDto> getItensDto() {
        return itensDto;
    }

    public void setItens(List<ItemDto> itens) {
        this.itensDto = itens;
    }
}

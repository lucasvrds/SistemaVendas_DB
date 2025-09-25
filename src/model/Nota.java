package model;

import model.ItemNota;
import model.Cliente;
import java.util.Date;
import java.util.List;

/**
 *
 * @author vitor e lucas
 */
public class Nota {
    private int codNota;
    private String data;    
    private int quantidade;
    
    private Cliente cliente;
    private List<ItemNota> itens;

    // Getters e Setters
    public int getCodNota() {
        return codNota;
    }

    public void setCodNota(int codNota) {
        this.codNota = codNota;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<ItemNota> getItens() {
        return itens;
    }

    public void setItens(List<ItemNota> itens) {
        this.itens = itens;
    }
}

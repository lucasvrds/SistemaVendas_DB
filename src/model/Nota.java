package model;

import java.time.LocalDate;
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
    private Cliente codCliente;
    private Produto codProduto;
    private LocalDate dataVenda;
    private List<ItemNota> itens;

    public Cliente getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(Cliente codCliente) {
        this.codCliente = codCliente;
    }

    public Produto getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Produto codProduto) {
        this.codProduto = codProduto;
    }

    public int getCodNota() {
        return codNota;
    }

    public void setCodNota(int codNota) {
        this.codNota = codNota;
    }

    public Cliente getCliente() {
        return codCliente;
    }

    public void setCliente(Cliente cliente) {
        this.codCliente = cliente;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public List<ItemNota> getItens() {
        return itens;
    }

    public void setItens(List<ItemNota> itens) {
        this.itens = itens;
    }   
}

package model;

/**
 *
 * @author vitor e lucas
 */
public class ItemNota {    
    private int codItemNota;
    private int quantidadeVendida;
    private Nota codNota;
    private Produto codProduto;

    public int getCodItemNota() {
        return codItemNota;
    }

    public void setCodItemNota(int codItemNota) {
        this.codItemNota = codItemNota;
    }

    public int getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(int quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public Nota getCodNota() {
        return codNota;
    }

    public void setCodNota(Nota codNota) {
        this.codNota = codNota;
    }

    public Produto getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Produto produto) {
        this.codProduto = produto;
    }  
}

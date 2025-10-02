package model;

/**
 *
 * @author vitor e lucas
 */
public class ItemNota {    
    private int codItemNota;    
    private int quantidade;
    
    private Produto codProduto;
    private Nota codNota;


    
    public Nota getCodNota() {
        return codNota;
    }

    public void setCodNota(Nota codNota) {
        this.codNota = codNota;
    }
    
    public int getCodItemNota() {
        return codItemNota;
    }

    public void setCodItemNota(int codItemNota) {
        this.codItemNota = codItemNota;
    }

    public Produto getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(Produto codProduto) {
        this.codProduto = codProduto;
    }   

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }  
}

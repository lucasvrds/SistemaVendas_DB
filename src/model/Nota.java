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
    private Date data;    
    private int quantidade;
    
    private Cliente cliente;
    private List<ItemNota> itens;
}

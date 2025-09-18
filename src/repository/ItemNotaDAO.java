package repository;

import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ItemNota;
import model.Nota;
import model.Produto;

/**
 *
 * @author vitor e lucas
 */
public class ItemNotaDAO {

    private Conexao conexao;
    private Connection conn;

    public ItemNotaDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserir(ItemNota item) {
        String sql = "INSERT INTO itemnota (codNota, codProduto, quantidadeVendida)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, item.getCodNota().getCodNota());
            stmt.setInt(2, item.getCodProduto().getCodProduto());
            stmt.setInt(3, item.getQuantidadeVendida()); 
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir item na nota: " + ex.getMessage());
        }
    }

    public List<ItemNota> listarPorNota(Nota nota) {
        List<ItemNota> lista = new ArrayList<>();
        String sql = "SELECT i.cod_item_nota, i.quantidade_vendida, " + "p.cod_produto, p.nome, p.descricao, p.preco_venda, p.quantidade_estoque " + "FROM item_nota i " + "JOIN produto p ON i.produto_id = p.cod_produto " + "WHERE i.nota_id = ?";
        try{
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, nota.getCodNota());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodProduto(rs.getInt("cod_produto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoVenda(rs.getDouble("preco_venda"));
                produto.setQtdEstoque(rs.getInt("quantidade_estoque"));

                ItemNota item = new ItemNota();
                item.setCodItemNota(rs.getInt("cod_item_nota"));
                item.setQuantidadeVendida(rs.getInt("quantidade_vendida"));
                item.setCodProduto(produto);
                item.setCodNota(nota);

                lista.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void excluirPorNota(Nota nota) {
        String sql = "DELETE FROM item_nota WHERE nota_id = ?";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nota.getCodNota());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

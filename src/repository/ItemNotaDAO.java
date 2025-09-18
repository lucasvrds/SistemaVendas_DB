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
        String sql = "SELECT i.codItemNota, i.quantidadeVendida, " + "p.codProduto, p.nome, p.descricao, p.precoVenda, p.qtdEstoque " + "FROM itemnota i "
                + "JOIN produto p ON i.codItemNota = p.codProduto " + "WHERE i.codItemNota = ?";
        try{
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, nota.getCodNota());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setCodProduto(rs.getInt("codProduto"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoVenda(rs.getDouble("precoVenda"));
                produto.setQtdEstoque(rs.getInt("qtdEstoque"));

                ItemNota item = new ItemNota();
                item.setCodItemNota(rs.getInt("codItemNota"));
                item.setQuantidadeVendida(rs.getInt("quantidadeVendida"));
                item.setCodProduto(produto);
                item.setCodNota(nota);

                lista.add(item);
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao listar item nota: " + ex.getMessage());
        }
        return lista;
    }

    public void excluirPorNota(Nota nota) {
        String sql = "DELETE FROM itemnota WHERE codNota = ?";
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nota.getCodNota());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Erro ao deletar item nota: " + ex.getMessage());
        }
    }

}

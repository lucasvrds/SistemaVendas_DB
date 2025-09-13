package repository;

import model.Produto;
import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author lucas & vitor
 */
public class ProdutoDAO {
    private Conexao conexao;
    private Connection conn;

    public ProdutoDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserir(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, precoVenda, quantidadeEstoque) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setInt(4, produto.getQtdEstoque());
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir produto: " + ex.getMessage());
        }
    }

    public Produto getProduto(int codProduto) {
        String sql = "SELECT * FROM produto WHERE codProduto = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, codProduto);
            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                Produto p = new Produto();
                p.setCodProduto(rs.getInt("codProduto"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setPrecoVenda(rs.getDouble("precoVenda"));
                p.setQtdEstoque(rs.getInt("quantidadeEstoque"));
                return p;
            }
            return null;
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar produto: " + ex.getMessage());
            return null;
        }
    }

    public void editar(Produto produto) {
        try {
            String sql = "UPDATE produto SET nome=?, descricao=?, precoVenda=?, quantidadeEstoque=? WHERE codProduto=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPrecoVenda());
            stmt.setInt(4, produto.getQtdEstoque());
            stmt.setInt(5, produto.getCodProduto());
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar produto: " + ex.getMessage());
        }
    }

    public void excluir(int codProduto) {
        try {
            String sql = "DELETE FROM produto WHERE codProduto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, codProduto);
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao excluir produto: " + ex.getMessage());
        }
    }
}

package repository;

import conexao.Conexao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Cliente;
import model.ItemNota;
import model.Nota;
import model.Produto;

/**
 *
 * @author lucas e vitor
 */
public class NotaDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public boolean inserir(Nota nota) {
        String sqlNota = "INSERT INTO nota (codCliente, dataVenda) VALUES (?, ?)";
        String sqlItem = "INSERT INTO itemnota (codNota, codProduto, quantidade) VALUES (?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            long idNotaGerada;
            try (PreparedStatement stmt = conn.prepareStatement(sqlNota, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, nota.getCliente().getCodCliente());
                stmt.setDate(2, Date.valueOf(nota.getDataVenda()));
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idNotaGerada = rs.getLong(1);
                    } else {
                        throw new SQLException("Falha ao criar a nota, nenhum ID obtido.");
                    }
                }
            }
            for (ItemNota item : nota.getItens()) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                    stmt.setLong(1, idNotaGerada);
                    stmt.setInt(2, item.getCodProduto().getCodProduto());
                    stmt.setInt(3, item.getQuantidade());
                    stmt.executeUpdate();
                }
            }
            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public List<Nota> listarTodas() {
        String sql = "SELECT " + "n.codNota, n.dataVenda, " + "c.codCliente, c.nome AS nome_cliente, "
                + "i.codItemNota, i.quantidade, "
                + "p.codProduto, p.nome AS nome_produto "
                + "FROM nota n "
                + "JOIN cliente c ON n.codCliente = c.codCliente "
                + "JOIN itemnota i ON n.codNota = i.codNota "
                + "JOIN produto p ON i.codProduto = p.codProduto "
                + "ORDER BY n.codNota DESC";

        Map<Integer, Nota> notasMap = new LinkedHashMap<>();

        try (PreparedStatement stmt = this.conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idNota = rs.getInt("codNota");
                Nota nota = notasMap.get(idNota);

                if (nota == null) {
                    Cliente cliente = new Cliente();
                    cliente.setCodCliente(rs.getInt("codCliente"));
                    cliente.setNome(rs.getString("nome_cliente"));

                    nota = new Nota();
                    nota.setCodNota(idNota);
                    nota.setDataVenda(rs.getDate("dataVenda").toLocalDate());
                    nota.setCliente(cliente);
                    nota.setItens(new ArrayList<>());

                    notasMap.put(idNota, nota);
                }

                Produto produto = new Produto();
                produto.setCodProduto(rs.getInt("codProduto"));
                produto.setNome(rs.getString("nome_produto"));

                ItemNota item = new ItemNota();
                item.setCodItemNota(rs.getInt("codItemNota"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setCodProduto(produto);
                nota.getItens().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(notasMap.values());
    }

    public boolean excluir(int codNota) {
        String sqlSelectItem = "SELECT codProduto, quantidade FROM itemnota WHERE codNota = ?";
        String sqlUpdateEstoque = "UPDATE produto SET quantidadeEstoque = quantidadeEstoque + ? WHERE codProduto = ?";
        String sqlDeleteItem = "DELETE FROM itemnota WHERE codNota = ?";
        String sqlDeleteNota = "DELETE FROM nota WHERE codNota = ?";

        try {
            conn.setAutoCommit(false);

            List<ItemNota> itensParaDevolver = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlSelectItem)) {
                stmt.setInt(1, codNota);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Produto p = new Produto();
                        p.setCodProduto(rs.getInt("codProduto"));

                        ItemNota item = new ItemNota();
                        item.setCodProduto(p);
                        item.setQuantidade(rs.getInt("quantidade"));
                        itensParaDevolver.add(item);
                    }
                }
            }

            if (itensParaDevolver.isEmpty()) {
                try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteNota)) {
                    stmt.setInt(1, codNota);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdateEstoque)) {
                for (ItemNota item : itensParaDevolver) {
                    stmt.setInt(1, item.getQuantidade());
                    stmt.setInt(2, item.getCodProduto().getCodProduto());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteItem)) {
                stmt.setInt(1, codNota);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteNota)) {
                stmt.setInt(1, codNota);
                stmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }
}

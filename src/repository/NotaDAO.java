package repository;

<<<<<<< HEAD
=======
import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.ItemNota;
import model.Nota;
import model.Produto;

>>>>>>> 41e5fe53886839fd9b232937fe22228379431e05
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
    
    
     public void insert(Nota nota) {
        String sqlNota =    "INSERT INTO nota (data, codCliente) VALUES (?, ?)";
        String sqlItem = "INSERT INTO itemnota (codNota, codProduto, quantidade) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmtNota = this.conn.prepareStatement(sqlNota, PreparedStatement.RETURN_GENERATED_KEYS);
            
            stmtNota.setDate(1, new java.sql.Date(nota.getData().getTime()));
            stmtNota.setInt(2, nota.getCliente().getCodCliente());
            stmtNota.executeUpdate();
            ResultSet rs = stmtNota.getGeneratedKeys();
            
            if (rs.next()) {
                int codNota = rs.getInt(1);
                nota.setCodNota(codNota);

                PreparedStatement stmtItem = this.conn.prepareStatement(sqlItem);
                for (ItemNota item : nota.getItens()) {
                    stmtItem.setInt(1, codNota);
                    stmtItem.setInt(2, item.getQuantidadeVendida());
                    stmtItem.setInt(3, item.getCodProduto().getCodProduto());
                    stmtItem.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir nota: " + ex.getMessage());
        }
    }

    public List<Nota> getAll() {
        List<Nota> notas = new ArrayList<>();
        String sql = "SELECT * FROM nota";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Nota nota = new Nota();
                nota.setCodNota(rs.getInt("codNota"));
                nota.setData(rs.getDate("data"));

                int codCliente = rs.getInt("codCliente");
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente cliente = clienteDAO.getCliente(codCliente);
                nota.setCliente(cliente);

                nota.setItens(getItensByNotaId(nota.getCodNota()));
                notas.add(nota);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar notas: " + ex.getMessage());
        }
        return notas;
    }

    private List<ItemNota> getItensByNotaId(int codNota) {
        List<ItemNota> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_nota WHERE codNota = ?";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setInt(1, codNota);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItemNota item = new ItemNota();
                item.setQuantidadeVendida(rs.getInt("quantidade"));
                
                int codProduto = rs.getInt("codProduto");
                ProdutoDAO produtoDAO = new ProdutoDAO();
                Produto produto = produtoDAO.getProdutoId(codProduto);
                item.setCodProduto(produto);

                itens.add(item);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao buscar notas por id: " + ex.getMessage());
        }
        return itens;
    }
}

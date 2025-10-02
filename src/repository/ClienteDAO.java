package repository;

import model.Cliente;
import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas & vitor
 */
public class ClienteDAO {

    private Conexao conexao;
    private Connection conn;

    public ClienteDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, endereco, email, telefone) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao inserir cliente: " + ex.getMessage());
        }
    }

    public Cliente getCliente(int codCliente) {
        String sql = "SELECT * FROM cliente WHERE codCliente = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, codCliente);
            ResultSet rs = stmt.executeQuery();

            if (rs.first()) {
                Cliente c = new Cliente();
                c.setCodCliente(rs.getInt("codCliente"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                return c;
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar cliente: " + ex.getMessage());
        }
        return null;
    }

    public void editar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome=?, endereco=?, email=?, telefone=? WHERE codCliente=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setInt(5, cliente.getCodCliente());
            stmt.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar cliente: " + ex.getMessage());
        }
    }

    public boolean excluir(int codCliente) {
        String sqlCheck = "SELECT COUNT(*) FROM nota WHERE codCliente = ?";
        try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
            stmtCheck.setInt(1, codCliente);
            try (ResultSet rs = stmtCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Tentativa de excluir cliente com notas associadas.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao verificar notas do cliente: " + ex.getMessage());
            return false;
        }

        String sqlDelete = "DELETE FROM cliente WHERE codCliente = ?";
        try (PreparedStatement stmtDelete = conn.prepareStatement(sqlDelete)) {
            stmtDelete.setInt(1, codCliente);
            int resultado = stmtDelete.executeUpdate();
            return resultado > 0;
        } catch (SQLException ex) {
            System.out.println("Erro ao excluir cliente: " + ex.getMessage());
            return false;
        }
    }

    public List<Cliente> getClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setCodCliente(rs.getInt("codCliente"));
                c.setNome(rs.getString("nome"));
                c.setEndereco(rs.getString("endereco"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                listaClientes.add(c);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao consultar clientes: " + ex.getMessage());
        }
        return listaClientes;
    }
}

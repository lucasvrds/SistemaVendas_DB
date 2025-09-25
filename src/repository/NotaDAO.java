package repository;

import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
        String sql = "INSERT INTO notas (data, quantidade, cod_cliente, nomeProduto) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nota.getData());
            stmt.setInt(2, nota.getQuantidade());
            stmt.setInt(3, nota.getCliente().getCodCliente());
            stmt.setString(4, nota.getNomeProduto());
            
            int resultado = stmt.executeUpdate();
            
            if (resultado > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    nota.setCodNota(rs.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Nota buscarPorId(int codNota) {
        String sql = "SELECT * FROM notas WHERE cod_nota = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codNota);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Nota nota = new Nota();
                nota.setCodNota(rs.getInt("cod_nota"));
                nota.setData(rs.getString("data"));
                nota.setQuantidade(rs.getInt("quantidade"));
                
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente cliente = clienteDAO.getCliente(rs.getInt("cod_cliente"));
                nota.setCliente(cliente);
                
                return nota;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Nota> listarTodas() {
        List<Nota> notas = new ArrayList<>();
        String sql = "SELECT * FROM notas";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Nota nota = new Nota();
                nota.setCodNota(rs.getInt("cod_nota"));
                nota.setData(rs.getString("data"));
                nota.setQuantidade(rs.getInt("quantidade"));
                
                ClienteDAO clienteDAO = new ClienteDAO();
                Cliente cliente = clienteDAO.getCliente(rs.getInt("cod_cliente"));
                nota.setCliente(cliente);
                
                notas.add(nota);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return notas;
    }
    
    public boolean atualizar(Nota nota) {
        String sql = "UPDATE notas SET data = ?, quantidade = ?, cod_cliente = ? WHERE cod_nota = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nota.getData());
            stmt.setInt(2, nota.getQuantidade());
            stmt.setInt(3, nota.getCliente().getCodCliente());
            stmt.setInt(4, nota.getCodNota());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deletar(int codNota) {
        String sql = "DELETE FROM notas WHERE cod_nota = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codNota);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

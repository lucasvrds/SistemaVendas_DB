package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author lucas & vitor
 */
public class Conexao {
    public Connection getConexao(){
        try{
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/sistemavendas?useTimezone=true&serverTimezone=UTC", "root", "");                    
            System.out.println("Connexao realizada com sucesso!");            
            return conn;
        }catch(Exception e){
            System.out.println("Erro ao conectar no bd " + e.getMessage());
            return null;
        }
    }
}
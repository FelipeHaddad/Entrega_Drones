import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    // Configurações do Banco de Dados
    // "jdbc:mysql://" -> Protocolo
    // "localhost:3306" -> Endereço do servidor e porta
    // "drone_delivery_db" -> Nome do banco que você criou com o script SQL
    private static final String URL = "jdbc:mysql://localhost:3306/drone_delivery_db";
    private static final String USUARIO = "user";
    private static final String SENHA = "senha";

    // Método estático para obter a conexão
    public static Connection getConnection() throws SQLException {
        try {
            // Tenta estabelecer a conexão usando os dados acima
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            // Se der erro, lança uma exceção explicando o problema
            throw new SQLException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
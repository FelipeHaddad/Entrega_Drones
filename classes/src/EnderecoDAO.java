import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class EnderecoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO Endereco (id, rua, numero, complemento, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM Endereco WHERE id = ?";

    public void inserir(Endereco endereco) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // 1. O DAO "chama" os Getters para saber o valor que está na memória
            // e passar para o Banco de Dados
            stmt.setString(1, endereco.getId().toString());
            stmt.setString(2, endereco.getRua());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setString(7, endereco.getCep());

            stmt.execute();
        }
    }

    public Endereco buscarPorId(UUID id) {
        Endereco endereco = null;
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // O banco devolve Strings e usa-se no CONSTRUTOR
                    // para transformar de volta em um objeto Java
                    endereco = new Endereco(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("rua"),
                            rs.getString("numero"),
                            rs.getString("complemento"),
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("cep")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return endereco;
    }
}
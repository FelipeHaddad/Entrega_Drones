import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsuarioDAO {

    private static final String SQL_INSERT =
            "INSERT INTO Usuario (id, email, senhaHash) VALUES (?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM Usuario WHERE id = ?";

    private static final String SQL_SELECT_BY_EMAIL =
            "SELECT * FROM Usuario WHERE email = ?";

    /**
     * Insere um novo usuário. 
     */
    public void inserir(Usuario usuario) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            stmt.setString(1, usuario.getId().toString());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenhaHash());

            stmt.executeUpdate();
        }
    }

    public Usuario buscarPorId(UUID id) {
        return buscar(SQL_SELECT_BY_ID, id.toString());
    }

    public Usuario buscarPorEmail(String email) {
        return buscar(SQL_SELECT_BY_EMAIL, email);
    }

    private Usuario buscar(String sql, String parametro) {
        Usuario usuario = null;
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, parametro);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String email = rs.getString("email");
                    String senhaHash = rs.getString("senhaHash");

                    usuario = new Usuario(id, email, senhaHash);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Usuario: " + e.getMessage());
        }
        return usuario;
    }
}
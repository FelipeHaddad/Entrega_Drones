import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegistroEntregaDAO {

    private static final String SQL_INSERT =
            "INSERT INTO RegistroEntrega (registroId, solicitacao_id, drone_id, status) VALUES (?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM RegistroEntrega WHERE registroId = ?";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE RegistroEntrega SET status = ?, motivo_falha = ? WHERE registroId = ?";

    public void inserir(RegistroEntrega registro) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            stmt.setString(1, registro.getRegistroId().toString());
            stmt.setString(2, registro.getSolicitacao().getSolicitacaoId().toString());
            stmt.setString(3, registro.getDrone().getDroneId().toString());
            stmt.setString(4, "EM_ANDAMENTO");

            stmt.executeUpdate();
        }
    }

    public void atualizarStatus(UUID id, String novoStatus, String motivo) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_STATUS)) {

            stmt.setString(1, novoStatus);
            stmt.setString(2, motivo);
            stmt.setString(3, id.toString());

            stmt.executeUpdate();
        }
    }

    public RegistroEntrega buscarPorId(UUID id) {
        RegistroEntrega registro = null;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID uuidRegistro = UUID.fromString(rs.getString("registroId"));
                    UUID uuidSolicitacao = UUID.fromString(rs.getString("solicitacao_id"));
                    UUID uuidDrone = UUID.fromString(rs.getString("drone_id"));

                    SolicitacaoEntregaDAO solDao = new SolicitacaoEntregaDAO();
                    SolicitacaoEntrega solicitacao = solDao.buscarPorId(uuidSolicitacao);

                    DroneDAO droneDao = new DroneDAO();
                    Drone drone = droneDao.buscarPorId(uuidDrone);

                    registro = new RegistroEntrega(uuidRegistro, solicitacao, drone);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar RegistroEntrega: " + e.getMessage());
        }
        return registro;
    }
}
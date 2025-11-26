import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DroneDAO {

    // 1. Strings SQL
    private static final String SQL_INSERT =
            "INSERT INTO Drone (droneId, bateriaPercentual, capacidadeMaxKg) VALUES (?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT droneId, bateriaPercentual, capacidadeMaxKg FROM Drone WHERE droneId = ?";

    public boolean inserir(Drone drone) {
        try (Connection conexao = ConexaoBD.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_INSERT)) {

            // Preenche os placeholders (?) do SQL com os dados do objeto Drone
            stmt.setString(1, drone.getDroneId().toString());
            stmt.setDouble(2, drone.getBateriaPercentual());
            stmt.setDouble(3, drone.getCapacidadeMaxKg());

            // Executa o comando SQL
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Drone: " + e.getMessage());
            return false;
        }
    }

    public List<RegistroEntrega> buscarHistoricoDoDrone(UUID droneId) {
        List<RegistroEntrega> lista = new ArrayList<>();
        String sql = "SELECT * FROM RegistroEntrega WHERE drone_id = ?";

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, droneId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Instancia o RegistroDAO para buscar o objeto completo
                    RegistroEntregaDAO regDAO = new RegistroEntregaDAO();
                    RegistroEntrega reg = regDAO.buscarPorId(UUID.fromString(rs.getString("registroId")));

                    if (reg != null) {
                        lista.add(reg);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        }
        return lista;
    }

    // Busca de Drone no Banco de Dados
    public Drone buscarPorId(UUID droneId) {
        Drone drone = null;

        try (Connection conexao = ConexaoBD.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_SELECT_BY_ID)) {

            // Atribui o ID do drone ao placeholder '?'
            stmt.setString(1, droneId.toString());

            // Executa a consulta
            try (ResultSet rs = stmt.executeQuery()) {

                // Se encontrar um resultado, mapeia para o objeto Drone
                if (rs.next()) {

                    // 1. Recupera os valores das colunas
                    String idStr = rs.getString("droneId");
                    double bateria = rs.getDouble("bateriaPercentual");
                    double capacidade = rs.getDouble("capacidadeMaxKg");

                    // 2. Cria o objeto Drone
                    drone = new Drone(
                            UUID.fromString(idStr),
                            bateria,
                            capacidade
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar Drone por ID: " + e.getMessage());
        }

        return drone;
    }
}
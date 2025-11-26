import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClienteDAO {

    private static final String SQL_SELECT_BY_ID =
            "SELECT clienteId, nomeCompleto, telefone, endereco_id FROM Cliente WHERE clienteId = ?";

    private static final String SQL_INSERT =
            "INSERT INTO Cliente (clienteId, nomeCompleto, telefone, endereco_id) VALUES (?, ?, ?, ?)";

    private static final String SQL_CONSULTAR_HISTORICO =
            "SELECT * FROM SolicitacaoEntrega WHERE cliente_id = ?";

    public Cliente buscarPorId(UUID id) {
        Cliente cliente = null;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Recupera dados simples
                    String nome = rs.getString("nomeCompleto");
                    String telefone = rs.getString("telefone");
                    String idEnderecoStr = rs.getString("endereco_id");

                    // 2. Recupera o Objeto Endereco (Dependência)
                    Endereco endereco = null;
                    if (idEnderecoStr != null) {
                        EnderecoDAO enderecoDAO = new EnderecoDAO();
                        // Chama o DAO de endereço
                        endereco = enderecoDAO.buscarPorId(UUID.fromString(idEnderecoStr));
                    }

                    // 3. Cria o objeto Cliente
                    cliente = new Cliente(id, nome, telefone, endereco);

                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Cliente: " + e.getMessage());
        }
        return cliente;
    }

    public void inserir(Cliente cliente) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            stmt.setString(1, cliente.getClienteId().toString());
            stmt.setString(2, "Nome Exemplo");
            stmt.setString(3, "Telefone Exemplo");


            if (cliente.getEndereco() != null) {
                stmt.setString(4, cliente.getEndereco().getId().toString());
            } else {
                stmt.setObject(4, null);
            }

            stmt.executeUpdate();
        }
    }

    public List<SolicitacaoEntrega> consultarHistoricoBD(UUID clienteId) {
        List<SolicitacaoEntrega> historico = new ArrayList<>();

        try (Connection conexao = ConexaoBD.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(SQL_CONSULTAR_HISTORICO)) {

            stmt.setString(1, clienteId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID idSolicitacao = UUID.fromString(rs.getString("solicitacaoId"));
                    double peso = rs.getDouble("pesoKg");

                    SolicitacaoEntrega solicitacao = new SolicitacaoEntrega(
                            idSolicitacao,
                            null,
                            null,
                            peso
                    );
                    historico.add(solicitacao);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico: " + e.getMessage());
        }
        return historico;
    }
}
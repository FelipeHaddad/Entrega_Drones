import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SolicitacaoEntregaDAO {

    private static final String SQL_INSERT =
            "INSERT INTO SolicitacaoEntrega (solicitacaoId, pesoKg, cliente_id, destino_id) VALUES (?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
            "SELECT * FROM SolicitacaoEntrega WHERE solicitacaoId = ?";

    /**
     * Insere uma solicitação no banco.
     */
    public void inserir(SolicitacaoEntrega solicitacao) throws SQLException {
        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            // 1. ID da própria solicitação
            stmt.setString(1, solicitacao.getSolicitacaoId().toString());

            // 2. Peso
            stmt.setDouble(2, solicitacao.getPesoKg());

            // 3. ID do Cliente (Extraído do objeto Cliente)
            stmt.setString(3, solicitacao.getCliente().getClienteId().toString());

            // 4. ID do Endereço de Destino (Extraído do objeto Endereco)
            stmt.setString(4, solicitacao.getDestino().getId().toString());

            stmt.executeUpdate();
        }
    }

    /**
     * Busca uma solicitação pelo ID e reconstrói os objetos dependentes.
     */
    public SolicitacaoEntrega buscarPorId(UUID id) {
        SolicitacaoEntrega solicitacao = null;

        try (Connection conn = ConexaoBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Passo A: Pegar os dados brutos
                    String idSolicitacaoStr = rs.getString("solicitacaoId");
                    double peso = rs.getDouble("pesoKg");
                    String idClienteStr = rs.getString("cliente_id");
                    String idDestinoStr = rs.getString("destino_id");

                    // Passo B: Converter IDs
                    UUID uuidSolicitacao = UUID.fromString(idSolicitacaoStr);
                    UUID uuidCliente = UUID.fromString(idClienteStr);
                    UUID uuidDestino = UUID.fromString(idDestinoStr);

                    // Passo C: Reconstruir as Dependências
                    ClienteDAO clienteDAO = new ClienteDAO();
                    Cliente cliente = clienteDAO.buscarPorId(uuidCliente);

                    EnderecoDAO enderecoDAO = new EnderecoDAO();
                    Endereco destino = enderecoDAO.buscarPorId(uuidDestino);

                    // Passo D: Criar o objeto final
                    solicitacao = new SolicitacaoEntrega(uuidSolicitacao, cliente, destino, peso);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar SolicitacaoEntrega: " + e.getMessage());
        }

        return solicitacao;
    }
}
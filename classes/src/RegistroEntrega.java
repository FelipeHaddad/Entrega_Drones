import java.util.*;
import java.util.UUID;
import java.sql.SQLException; // Import necessário para tratar erros do banco

public class RegistroEntrega {
    private UUID registroId;
    private SolicitacaoEntrega solicitacao;
    private Drone drone;

    public RegistroEntrega(UUID registroId, SolicitacaoEntrega solicitacao, Drone drone) {
        this.registroId = registroId;
        this.solicitacao = solicitacao;
        this.drone = drone;
    }

    public void marcarEntregue() {
        System.out.println("Processando entrega " + registroId + "...");

        RegistroEntregaDAO dao = new RegistroEntregaDAO();
        try {
            dao.atualizarStatus(this.registroId, "ENTREGUE", null);
            System.out.println("Sucesso: Status atualizado no Banco de Dados para 'ENTREGUE'.");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    public void marcarFalha(String motivo) {
        System.out.println("Processando falha da entrega " + registroId + "...");

        RegistroEntregaDAO dao = new RegistroEntregaDAO();
        try {
            dao.atualizarStatus(this.registroId, "FALHA", motivo);
            System.out.println("Aviso: Status atualizado no Banco de Dados para 'FALHA'. Motivo: " + motivo);
        } catch (SQLException e) {
            System.err.println("Erro ao salvar no banco: " + e.getMessage());
        }
    }

    public UUID getRegistroId() { return registroId; }
    public SolicitacaoEntrega getSolicitacao() { return solicitacao; }
    public Drone getDrone() { return drone; }
}
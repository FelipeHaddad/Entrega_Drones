import java.util.*;
import java.util.UUID;

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
        System.out.println("Entrega " + registroId + " concluída com sucesso.");
    }

    public void marcarFalha(String motivo) {
        System.out.println("Entrega " + registroId + " falhou: " + motivo);
    }

    public UUID getRegistroId() { return registroId; }
    public SolicitacaoEntrega getSolicitacao() { return solicitacao; }
    public Drone getDrone() { return drone; }
}

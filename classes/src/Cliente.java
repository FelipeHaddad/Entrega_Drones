import java.util.*;
import java.util.UUID;

public class Cliente {
    private UUID clienteId;
    private String nomeCompleto;
    private String telefone;
    private Endereco endereco;
    private List<SolicitacaoEntrega> solicitacoes;

    public Cliente(UUID clienteId, String nomeCompleto, String telefone, Endereco endereco) {
        this.clienteId = clienteId;
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
        this.endereco = endereco;
        this.solicitacoes = new ArrayList<>();
    }

    public List<SolicitacaoEntrega> consultarHistorico() {
        return solicitacoes;
    }

    public void adicionarSolicitacao(SolicitacaoEntrega s) {
        solicitacoes.add(s);
    }

    // Getters
    public UUID getClienteId() { return clienteId; }
    public Endereco getEndereco() { return endereco; }
}

import java.util.*;

import java.util.UUID;

public class SolicitacaoEntrega {
    private UUID solicitacaoId;
    private Cliente cliente;
    private Endereco destino;
    private double pesoKg;

    public SolicitacaoEntrega(UUID solicitacaoId, Cliente cliente, Endereco destino, double pesoKg) {
        this.solicitacaoId = solicitacaoId;
        this.cliente = cliente;
        this.destino = destino;
        this.pesoKg = pesoKg;
    }

    public void validar() throws Exception {
        if (pesoKg <= 0) {
            throw new Exception("Peso do pacote inválido");
        }
        if (destino == null || destino.getCep() == null || destino.getCep().isEmpty()) {
            throw new Exception("Endereço inválido");
        }
    }

    public UUID getSolicitacaoId() { return solicitacaoId; }
    public Cliente getCliente() { return cliente; }
    public Endereco getDestino() { return destino; }
    public double getPesoKg() { return pesoKg; }
}
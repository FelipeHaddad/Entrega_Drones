import java.util.*;
import java.util.UUID;

 class Drone {
    private UUID droneId;
    private double bateriaPercentual;
    private double capacidadeMaxKg;
    private List<RegistroEntrega> historico;

    public Drone(UUID droneId, double bateriaPercentual, double capacidadeMaxKg) {
        this.droneId = droneId;
        this.bateriaPercentual = bateriaPercentual;
        this.capacidadeMaxKg = capacidadeMaxKg;
        this.historico = new ArrayList<>();
    }

    public boolean estaDisponivel() {
        return bateriaPercentual > 20;
    }

    public boolean podeTransportar(double pesoKg) {
        return pesoKg <= capacidadeMaxKg;
    }

    public boolean reservarPara(UUID solicitacaoId) {
        if (estaDisponivel()) {
            System.out.println("Drone " + droneId + " reservado para solicitação " + solicitacaoId);
            return true;
        }
        return false;
    }

    public List<RegistroEntrega> consultarHistorico() {
        return historico;
    }

    public void adicionarRegistro(RegistroEntrega registro) {
        historico.add(registro);
    }

    public UUID getDroneId() { return droneId; }
}

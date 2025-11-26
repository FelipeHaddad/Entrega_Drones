import java.util.*;
import java.util.UUID;

public class Drone {
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

    public void salvar() {
        DroneDAO dao = new DroneDAO();
        boolean sucesso = dao.inserir(this);

        if (sucesso) {
            System.out.println("Drone " + droneId + " cadastrado com sucesso no Banco de Dados!");
        } else {
            System.err.println("Erro ao salvar o Drone " + droneId);
        }
    }

    public List<RegistroEntrega> consultarHistorico() {
        DroneDAO dao = new DroneDAO();

        List<RegistroEntrega> historicoDoBanco = dao.buscarHistoricoDoDrone(this.droneId);

        this.historico = historicoDoBanco;

        return this.historico;
    }

    public void adicionarRegistro(RegistroEntrega registro) {
        historico.add(registro);
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

    public UUID getDroneId() {
        return droneId;
    }

    public double getBateriaPercentual() {
        return bateriaPercentual;
    }

    public double getCapacidadeMaxKg() {
        return capacidadeMaxKg;
    }
}
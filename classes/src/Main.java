import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    // Scanner global para ler dados do teclado
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ENTREGA DE DRONES - ONLINE ===");

        // Verifica driver do banco
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ERRO: Driver MySQL não encontrado! Verifique o pom.xml ou bibliotecas.");
            return;
        }

        boolean rodando = true;

        while (rodando) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Cadastrar Novo Cliente (e Usuário)");
            System.out.println("2. Cadastrar Novo Drone");
            System.out.println("3. Realizar Fluxo de Entrega Completo");
            System.out.println("4. Consultar Histórico de Cliente");
            System.out.println("5. Consultar Histórico de Drone");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            try {
                switch (opcao) {
                    case "1":
                        cadastrarCliente();
                        break;
                    case "2":
                        cadastrarDrone();
                        break;
                    case "3":
                        realizarFluxoEntrega(); // Nome alterado para refletir que faz tudo
                        break;
                    case "4":
                        consultarHistoricoCliente();
                        break;
                    case "5":
                        consultarHistoricoDrone(); // Nova opção!
                        break;
                    case "0":
                        rodando = false;
                        System.out.println("Sistema encerrado.");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.err.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace(); // Ajuda a achar o erro no console
            }
        }
    }

    // --- 1. CADASTRAR CLIENTE ---
    private static void cadastrarCliente() throws Exception {
        System.out.println("\n--- Cadastro de Cliente ---");

        System.out.print("Rua: ");
        String rua = scanner.nextLine();
        System.out.print("CEP: ");
        String cep = scanner.nextLine();

        // 1. Salva Endereço
        UUID idEndereco = UUID.randomUUID();
        Endereco endereco = new Endereco(idEndereco, rua, "000", "Casa", "CidadeDemo", "UF", cep);
        new EnderecoDAO().inserir(endereco);

        // 2. Dados Pessoais
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Nome Completo: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String fone = scanner.nextLine();

        UUID idUnico = UUID.randomUUID();

        // 3. Salva Usuário (Pai) - Requer classe Usuario e UsuarioDAO
        Usuario usuario = new Usuario(idUnico, email, senha);
        usuario.salvar();

        // 4. Salva Cliente (Filho)
        Cliente cliente = new Cliente(idUnico, nome, fone, endereco);
        new ClienteDAO().inserir(cliente);

        System.out.println(">> Sucesso! ID do Cliente: " + idUnico);
        System.out.println("(Copie este ID para usar depois)");
    }

    // --- 2. CADASTRAR DRONE ---
    private static void cadastrarDrone() {
        System.out.println("\n--- Cadastro de Drone ---");

        UUID idDrone = UUID.randomUUID();
        // Criando drone com 100% de bateria e 20kg de capacidade
        Drone drone = new Drone(idDrone, 100.0, 20.0);

        // AQUI USAMOS O MÉTODO DA SUA CLASSE DRONE
        drone.salvar();

        System.out.println(">> Drone cadastrado! ID: " + idDrone);
        System.out.println("(Copie este ID para usar depois)");
    }

    // --- 3. FLUXO DE ENTREGA (Solicitação -> Registro -> Entrega) ---
    private static void realizarFluxoEntrega() throws Exception {
        System.out.println("\n--- Fluxo de Entrega ---");

        // A. Busca Cliente
        System.out.print("ID do Cliente (UUID): ");
        String idCliStr = scanner.nextLine();
        Cliente cliente = new ClienteDAO().buscarPorId(UUID.fromString(idCliStr));

        if (cliente == null) {
            System.out.println("X Cliente não encontrado.");
            return;
        }

        // B. Cria Destino
        System.out.print("CEP de Destino: ");
        String cepDestino = scanner.nextLine();
        UUID idDestino = UUID.randomUUID();
        Endereco destino = new Endereco(idDestino, "Rua Dest", "1", null, "City", "UF", cepDestino);
        new EnderecoDAO().inserir(destino);

        // C. Cria Solicitação
        System.out.print("Peso do pacote (kg): ");
        double peso = Double.parseDouble(scanner.nextLine());

        // Validação de Segurança (Simulada)
        ServicoSeguranca seguranca = new ServicoSeguranca();
        if (!seguranca.verificarAssinatura("Dados", "ASSINATURA_VALIDA_123")) {
            System.out.println("X Erro de Segurança.");
            return;
        }

        UUID idSol = UUID.randomUUID();
        SolicitacaoEntrega solicitacao = new SolicitacaoEntrega(idSol, cliente, destino, peso);
        solicitacao.salvar(); // Salva a solicitação no banco

        // --- A PARTIR DAQUI ENTRA A LÓGICA DE DRONE E REGISTRO ---

        System.out.println("\n...Procurando Drone...");
        System.out.print("Digite o ID do Drone para alocar: ");
        String idDroneStr = scanner.nextLine();

        // Busca o Drone no Banco
        Drone drone = new DroneDAO().buscarPorId(UUID.fromString(idDroneStr));

        if (drone != null) {
            // 1. Tenta Reservar (Lógica de Negócio)
            if (drone.reservarPara(idSol)) {

                UUID idReg = UUID.randomUUID();

                // 2. Cria o Registro de Entrega
                RegistroEntrega registro = new RegistroEntrega(idReg, solicitacao, drone);

                // 3. Salva o Registro no Banco (Persistência)
                RegistroEntregaDAO regDAO = new RegistroEntregaDAO();
                regDAO.inserir(registro);

                // 4. Atualiza a memória do Drone (Método que você perguntou!)
                drone.adicionarRegistro(registro);

                System.out.println(">> Drone reservado e Registro criado!");

                // 5. Simula a finalização da entrega
                System.out.println("...Drone voando...");

                // Atualiza status no banco para ENTREGUE
                registro.marcarEntregue();

            } else {
                System.out.println("X Drone indisponível (Bateria fraca ou Peso excedido).");
            }
        } else {
            System.out.println("X Drone não encontrado no banco.");
        }
    }

    // --- 4. CONSULTAR HISTÓRICO CLIENTE ---
    private static void consultarHistoricoCliente() {
        System.out.print("\nID do Cliente: ");
        String idStr = scanner.nextLine();
        // Cria objeto dummy só com ID para buscar
        Cliente cliente = new Cliente(UUID.fromString(idStr), null, null, null);

        List<SolicitacaoEntrega> lista = cliente.consultarHistorico();

        if (lista.isEmpty()) System.out.println("Nenhum histórico.");
        else {
            System.out.println("Histórico Cliente (" + lista.size() + "):");
            for (SolicitacaoEntrega s : lista) {
                System.out.println("- Solicitacao: " + s.getSolicitacaoId() + " | Peso: " + s.getPesoKg());
            }
        }
    }

    // --- 5. CONSULTAR HISTÓRICO DRONE ---
    private static void consultarHistoricoDrone() {
        System.out.print("\nID do Drone: ");
        String idStr = scanner.nextLine();

        // Busca o drone completo no banco primeiro
        Drone drone = new DroneDAO().buscarPorId(UUID.fromString(idStr));

        if (drone != null) {
            // Testando o método que arrumamos para buscar do banco
            List<RegistroEntrega> lista = drone.consultarHistorico();

            System.out.println("Histórico do Drone " + drone.getBateriaPercentual() + "% bat (" + lista.size() + " entregas):");
            for (RegistroEntrega r : lista) {
                // Como não carregamos todos os objetos profundos na lista simples, mostramos IDs
                System.out.println("- Registro ID: " + r.getRegistroId());
            }
        } else {
            System.out.println("Drone não encontrado.");
        }
    }
}
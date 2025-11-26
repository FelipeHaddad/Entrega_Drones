public class ServicoSeguranca {

    private static final String TOKEN_SECRETO = "ASSINATURA_VALIDA_123";

    public boolean verificarAssinatura(Object requisicao, String assinatura) {

        System.out.println("Segurança: Verificando integridade da requisição...");

        // 1. Validação básica
        if (assinatura == null || assinatura.isEmpty()) {
            System.out.println("Segurança: Falha - Assinatura vazia.");
            return false;
        }

        // 2. Simulação de checagem criptográfica
        // Se a assinatura for igual ao nosso token, passa. Se for diferente, bloqueia.
        if (assinatura.equals(TOKEN_SECRETO)) {
            System.out.println("Segurança: Sucesso - Assinatura válida.");
            return true;
        } else {
            System.out.println("Segurança: Falha - Assinatura inválida/adulterada.");
            return false;
        }
    }
}
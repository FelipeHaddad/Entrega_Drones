import java.util.*;
import java.util.UUID;
import java.sql.SQLException; // Import necessário para erros de banco

public class Usuario {
    private UUID id;
    private String email;
    private String senhaHash;

    public Usuario(UUID id, String email, String senhaHash) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public boolean autenticar(String senha) {
        return Objects.equals(this.senhaHash, senha);
    }

    public void salvar() {
        UsuarioDAO dao = new UsuarioDAO();
        try {
            dao.inserir(this);
            System.out.println("Usuário salvo com sucesso no banco!");
        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    public UUID getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
}
import java.util.*;
import java.util.UUID;

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

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

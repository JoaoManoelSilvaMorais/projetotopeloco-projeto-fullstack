package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.PermissaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.UsuarioRepository;

@Component
public class ConfiguracaoInicial implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PermissaoRepository permissaoRepository;
    private final PasswordEncoder passwordEncoder;

    public ConfiguracaoInicial(UsuarioRepository usuarioRepository,
                               PermissaoRepository permissaoRepository,
                               PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        PermissaoModel adminPermissao = permissaoRepository.findByNome("ADMIN")
                .orElseGet(() -> permissaoRepository.save(new PermissaoModel("ADMIN", "Permissão de administrador")));

        PermissaoModel userPermissao = permissaoRepository.findByNome("USER")
                .orElseGet(() -> permissaoRepository.save(new PermissaoModel("USER", "Permissão padrão de usuário")));

        // Admin user
        UsuarioModel admin = usuarioRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new UsuarioModel("Administrador", "admin", passwordEncoder.encode("admin123"), "admin@garagelog.com");
            admin.setPermissoes(new HashSet<>(Set.of(adminPermissao)));
            usuarioRepository.save(admin);
        } else {
            boolean needsUpdate = false;
            if (admin.getSenha() == null || (!admin.getSenha().startsWith("$2a$") && !admin.getSenha().startsWith("$2b$"))) {
                admin.setSenha(passwordEncoder.encode("admin123"));
                needsUpdate = true;
            }
            if (admin.getPermissoes() == null || admin.getPermissoes().stream().noneMatch(p -> "ADMIN".equalsIgnoreCase(p.getNome()))) {
                if (admin.getPermissoes() == null) {
                    admin.setPermissoes(new HashSet<>());
                }
                admin.getPermissoes().add(adminPermissao);
                needsUpdate = true;
            }
            if (needsUpdate) {
                usuarioRepository.save(admin);
            }
        }

        // Standard user
        UsuarioModel user = usuarioRepository.findByUsername("user").orElse(null);
        if (user == null) {
            user = new UsuarioModel("Usuário Comum", "user", passwordEncoder.encode("user123"), "user@garagelog.com");
            user.setPermissoes(new HashSet<>(Set.of(userPermissao)));
            usuarioRepository.save(user);
        } else {
            boolean needsUpdate = false;
            if (user.getSenha() == null || (!user.getSenha().startsWith("$2a$") && !user.getSenha().startsWith("$2b$"))) {
                user.setSenha(passwordEncoder.encode("user123"));
                needsUpdate = true;
            }
            if (user.getPermissoes() == null || user.getPermissoes().stream().noneMatch(p -> "USER".equalsIgnoreCase(p.getNome()))) {
                if (user.getPermissoes() == null) {
                    user.setPermissoes(new HashSet<>());
                }
                user.getPermissoes().add(userPermissao);
                needsUpdate = true;
            }
            if (needsUpdate) {
                usuarioRepository.save(user);
            }
        }
    }
}

package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.PermissaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    public static final String SENHA_PADRAO = "123456";

    private final UsuarioRepository usuarioRepository;
    private final PermissaoRepository permissaoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PermissaoRepository permissaoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioModel atribuirPermissoes(Long usuarioId, List<Long> idsPermissoes) {
        UsuarioModel usuario = buscarPorId(usuarioId);
        List<Long> ids = idsPermissoes == null ? List.of() : idsPermissoes;
        Set<PermissaoModel> permissoes = new HashSet<>(permissaoRepository.findAllById(ids));
        Set<Long> idsEncontrados = permissoes.stream()
                .map(PermissaoModel::getId)
                .collect(Collectors.toSet());

        if (idsEncontrados.size() != ids.stream().distinct().count()
                || !idsEncontrados.containsAll(ids)) {
            throw new IllegalArgumentException("Uma ou mais permissões não foram encontradas.");
        }

        usuario.getPermissoes().clear();
        usuario.getPermissoes().addAll(permissoes);
        return usuarioRepository.saveAndFlush(usuario);
    }

    @Transactional
    public UsuarioModel criar(UsuarioModel usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new IllegalArgumentException("O username é obrigatório.");
        }
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este username");
        }

        String senhaInformada = usuario.getSenha();
        if (senhaInformada == null || senhaInformada.isBlank()) {
            usuario.setSenha(passwordEncoder.encode(SENHA_PADRAO));
            usuario.setPrecisaTrocarSenha(true);
        } else {
            boolean ehSenhaPadrao = SENHA_PADRAO.equals(senhaInformada);
            usuario.setSenha(passwordEncoder.encode(senhaInformada));
            if (ehSenhaPadrao) {
                usuario.setPrecisaTrocarSenha(true);
            }
        }

        if (usuario.getPermissoes() == null || usuario.getPermissoes().isEmpty()) {
            permissaoRepository.findByNome("USER").ifPresent(permissao -> {
                if (usuario.getPermissoes() == null) {
                    usuario.setPermissoes(new HashSet<>());
                }
                usuario.getPermissoes().add(permissao);
            });
        }

        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(Long id, UsuarioModel dados) {
        UsuarioModel usuario = buscarPorId(id);
        if (dados.getEmail() != null
                && usuarioRepository.existsByEmailAndIdNot(dados.getEmail(), id)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail");
        }
        if (dados.getUsername() != null
                && usuarioRepository.existsByUsernameAndIdNot(dados.getUsername(), id)) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este username");
        }
        usuario.setNome(dados.getNome());
        usuario.setUsername(dados.getUsername());
        usuario.setEmail(dados.getEmail());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            boolean ehSenhaPadrao = SENHA_PADRAO.equals(dados.getSenha());
            usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
            usuario.setPrecisaTrocarSenha(ehSenhaPadrao);
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioModel alterarSenha(String username, String senhaAtual, String novaSenha) {
        if (novaSenha == null || novaSenha.isBlank() || novaSenha.trim().length() < 6) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 6 caracteres.");
        }
        UsuarioModel usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
        if (senhaAtual == null || usuario.getSenha() == null
                || !passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }
        if (passwordEncoder.matches(novaSenha, usuario.getSenha()) || SENHA_PADRAO.equals(novaSenha)) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha padrão e da senha atual.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setPrecisaTrocarSenha(false);
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscarPorId(id));
    }
}

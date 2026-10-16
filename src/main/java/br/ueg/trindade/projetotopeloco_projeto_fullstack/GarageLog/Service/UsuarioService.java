package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.PermissaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final PermissaoRepository permissaoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PermissaoRepository permissaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
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

    public UsuarioModel criar(UsuarioModel usuario) {
        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail");
        }
        if (usuario.getUsername() != null && usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este username");
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(Long id, UsuarioModel dados) {
        UsuarioModel usuario = buscarPorId(id);
        if (dados.getEmail() != null
                && usuarioRepository.existsByEmailAndIdNot(dados.getEmail(), id)) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail");
        }
        if (dados.getUsername() != null
                && usuarioRepository.existsByUsernameAndIdNot(dados.getUsername(), id)) {
            throw new RuntimeException("Já existe um usuário cadastrado com este username");
        }
        usuario.setNome(dados.getNome());
        usuario.setUsername(dados.getUsername());
        usuario.setEmail(dados.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscarPorId(id));
    }
}

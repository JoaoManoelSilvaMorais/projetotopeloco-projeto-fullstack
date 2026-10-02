package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    public UsuarioModel buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioModel criar(UsuarioModel usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail");
        }
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(Long id, UsuarioModel dados) {
        UsuarioModel usuario = buscarPorId(id);
        usuario.setNome(dados.getNome());
        usuario.setUsername(dados.getUsername());
        usuario.setEmail(dados.getEmail());
        return usuarioRepository.save(usuario);
    }

    public void excluir(Long id) {
        usuarioRepository.delete(buscarPorId(id));
    }
}

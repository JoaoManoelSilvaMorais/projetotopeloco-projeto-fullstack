package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.PermissaoRepository;

@Service
public class PermissaoService {
    @Autowired
    private final PermissaoRepository permissaoRepository;

    public PermissaoService(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Transactional(readOnly = true)
    public List<PermissaoModel> listarTodos() {
        return permissaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PermissaoModel buscarPorId(Long id) {
        return permissaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada"));
    }

    @Transactional
    public PermissaoModel criar(PermissaoModel permissao) {
        return permissaoRepository.save(permissao);
    }

    @Transactional
    public PermissaoModel atualizar(Long id, PermissaoModel dados) {
        PermissaoModel permissao = buscarPorId(id);
        permissao.setNome(dados.getNome());
        permissao.setDescricao(dados.getDescricao());
        return permissaoRepository.save(permissao);
    }

    @Transactional
    public void excluir(Long id) {
        permissaoRepository.delete(buscarPorId(id));
    }
}

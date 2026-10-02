package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.ServicoManutencaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
public class ServicoManutencaoService {
    private final ServicoManutencaoRepository servicoRepository;
    private final VeiculoRepository veiculoRepository;

    public ServicoManutencaoService(ServicoManutencaoRepository servicoRepository,
            VeiculoRepository veiculoRepository) {
        this.servicoRepository = servicoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public List<ServicoManutencaoModel> listarTodos() {
        return servicoRepository.findAll();
    }

    public List<ServicoManutencaoModel> listarPorVeiculo(UUID veiculoId) {
        return servicoRepository.findByVeiculoId(veiculoId);
    }

    public ServicoManutencaoModel buscarPorId(UUID id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public ServicoManutencaoModel criar(ServicoManutencaoModel servico, UUID veiculoId) {
        servico.setVeiculo(buscarVeiculo(veiculoId));
        return servicoRepository.save(servico);
    }

    public ServicoManutencaoModel atualizar(UUID id, ServicoManutencaoModel dados, UUID veiculoId) {
        ServicoManutencaoModel servico = buscarPorId(id);
        servico.setTipoIntervencao(dados.getTipoIntervencao());
        servico.setDescricao(dados.getDescricao());
        servico.setDataRealizacao(dados.getDataRealizacao());
        servico.setKmAtual(dados.getKmAtual());
        servico.setProximaRevisaoKm(dados.getProximaRevisaoKm());
        servico.setCustoPecas(dados.getCustoPecas());
        servico.setCustoMaoDeObra(dados.getCustoMaoDeObra());
        servico.setVeiculo(buscarVeiculo(veiculoId));
        return servicoRepository.save(servico);
    }

    public void excluir(UUID id) {
        servicoRepository.delete(buscarPorId(id));
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
}

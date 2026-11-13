package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.ServicoManutencaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
@Transactional
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
        if (id == null) {
            throw new IllegalArgumentException("ID do serviço não pode ser nulo");
        }
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
    }

    public ServicoManutencaoModel criar(ServicoManutencaoModel servico, UUID veiculoId) {
        if (veiculoId == null) {
            throw new IllegalArgumentException("ID do veículo é obrigatório");
        }
        validarServico(servico);
        VeiculoModel veiculo = buscarVeiculo(veiculoId);
        servico.setVeiculo(veiculo);
        return servicoRepository.save(servico);
    }

    public ServicoManutencaoModel atualizar(UUID id, ServicoManutencaoModel dados, UUID veiculoId) {
        if (veiculoId == null) {
            throw new IllegalArgumentException("ID do veículo é obrigatório");
        }
        validarServico(dados);
        ServicoManutencaoModel servico = buscarPorId(id);
        VeiculoModel veiculo = buscarVeiculo(veiculoId);
        servico.setTipoIntervencao(dados.getTipoIntervencao());
        servico.setDescricao(dados.getDescricao());
        servico.setDataRealizacao(dados.getDataRealizacao());
        servico.setKmAtual(dados.getKmAtual());
        servico.setProximaRevisaoKm(dados.getProximaRevisaoKm());
        servico.setCustoPecas(dados.getCustoPecas());
        servico.setCustoMaoDeObra(dados.getCustoMaoDeObra());
        servico.setVeiculo(veiculo);
        return servicoRepository.save(servico);
    }

    public void excluir(UUID id) {
        servicoRepository.delete(buscarPorId(id));
    }

    public BigDecimal calcularCustoTotal(UUID id) {
        return buscarPorId(id).calcularCustoTotal();
    }

    public Boolean verificarAlertaRevisao(UUID id, Integer kmVeiculoAtual) {
        return buscarPorId(id).verificarAlertaRevisao(kmVeiculoAtual);
    }

    private void validarServico(ServicoManutencaoModel servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Dados do serviço são obrigatórios");
        }
        if (servico.getTipoIntervencao() == null) {
            throw new IllegalArgumentException("Tipo de intervenção é obrigatório");
        }
        if (servico.getDescricao() == null || servico.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }
        if (servico.getDataRealizacao() == null) {
            throw new IllegalArgumentException("Data de realização é obrigatória");
        }
        if (servico.getKmAtual() != null && servico.getKmAtual() < 0) {
            throw new IllegalArgumentException("Quilometragem atual não pode ser negativa");
        }
        if (servico.getProximaRevisaoKm() != null && servico.getProximaRevisaoKm() < 0) {
            throw new IllegalArgumentException("Quilometragem da próxima revisão não pode ser negativa");
        }
        if (servico.getCustoPecas() != null && servico.getCustoPecas().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Custo de peças não pode ser negativo");
        }
        if (servico.getCustoMaoDeObra() != null && servico.getCustoMaoDeObra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Custo de mão de obra não pode ser negativo");
        }
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
}

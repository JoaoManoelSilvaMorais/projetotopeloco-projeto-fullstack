package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.DespesaRecorrenteRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
@Transactional
public class DespesaRecorrenteService {
    private final DespesaRecorrenteRepository despesaRepository;
    private final VeiculoRepository veiculoRepository;

    public DespesaRecorrenteService(DespesaRecorrenteRepository despesaRepository,
            VeiculoRepository veiculoRepository) {
        this.despesaRepository = despesaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public List<DespesaRecorrenteModel> listarTodos() {
        return despesaRepository.findAll();
    }

    public List<DespesaRecorrenteModel> listarPorVeiculo(UUID veiculoId) {
        return despesaRepository.findByVeiculoId(veiculoId);
    }

    public DespesaRecorrenteModel buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da despesa não pode ser nulo");
        }
        return despesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
    }

    public DespesaRecorrenteModel criar(DespesaRecorrenteModel despesa, UUID veiculoId) {
        if (veiculoId == null) {
            throw new IllegalArgumentException("ID do veículo é obrigatório");
        }
        validarDespesa(despesa);
        VeiculoModel veiculo = buscarVeiculo(veiculoId);
        despesa.setVeiculo(veiculo);
        return despesaRepository.save(despesa);
    }

    public DespesaRecorrenteModel atualizar(UUID id, DespesaRecorrenteModel dados, UUID veiculoId) {
        if (veiculoId == null) {
            throw new IllegalArgumentException("ID do veículo é obrigatório");
        }
        validarDespesa(dados);
        DespesaRecorrenteModel despesa = buscarPorId(id);
        VeiculoModel veiculo = buscarVeiculo(veiculoId);
        despesa.setTipo(dados.getTipo());
        despesa.setValorMensal(dados.getValorMensal());
        despesa.setDiaVencimento(dados.getDiaVencimento());
        despesa.setStatusAtivo(dados.getStatusAtivo());
        despesa.setVeiculo(veiculo);
        return despesaRepository.save(despesa);
    }

    public void excluir(UUID id) {
        despesaRepository.delete(buscarPorId(id));
    }

    private void validarDespesa(DespesaRecorrenteModel despesa) {
        if (despesa == null) {
            throw new IllegalArgumentException("Dados da despesa são obrigatórios");
        }
        if (despesa.getTipo() == null) {
            throw new IllegalArgumentException("Tipo de despesa é obrigatório");
        }
        if (despesa.getValorMensal() == null) {
            throw new IllegalArgumentException("Valor mensal é obrigatório");
        }
        if (despesa.getValorMensal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor mensal não pode ser negativo");
        }
        if (despesa.getDiaVencimento() == null) {
            throw new IllegalArgumentException("Dia de vencimento é obrigatório");
        }
        if (despesa.getDiaVencimento() < 1 || despesa.getDiaVencimento() > 31) {
            throw new IllegalArgumentException("Dia de vencimento deve estar entre 1 e 31");
        }
        if (despesa.getStatusAtivo() == null) {
            despesa.setStatusAtivo(true);
        }
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
}

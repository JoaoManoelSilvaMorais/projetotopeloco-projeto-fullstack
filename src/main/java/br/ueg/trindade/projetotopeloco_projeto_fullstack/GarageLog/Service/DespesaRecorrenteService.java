package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.DespesaRecorrenteRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
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
        return despesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
    }

    public DespesaRecorrenteModel criar(DespesaRecorrenteModel despesa, UUID veiculoId) {
        despesa.setVeiculo(buscarVeiculo(veiculoId));
        return despesaRepository.save(despesa);
    }

    public DespesaRecorrenteModel atualizar(UUID id, DespesaRecorrenteModel dados, UUID veiculoId) {
        DespesaRecorrenteModel despesa = buscarPorId(id);
        despesa.setTipo(dados.getTipo());
        despesa.setValorMensal(dados.getValorMensal());
        despesa.setDiaVencimento(dados.getDiaVencimento());
        despesa.setStatusAtivo(dados.getStatusAtivo());
        despesa.setVeiculo(buscarVeiculo(veiculoId));
        return despesaRepository.save(despesa);
    }

    public void excluir(UUID id) {
        despesaRepository.delete(buscarPorId(id));
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }
}

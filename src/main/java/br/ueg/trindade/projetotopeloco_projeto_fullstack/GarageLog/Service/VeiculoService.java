package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<VeiculoModel> listarTodos() {
        return veiculoRepository.findAll();
    }

    public VeiculoModel buscarPorId(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }

    public VeiculoModel criar(VeiculoModel veiculo) {
        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new RuntimeException("Já existe um veículo cadastrado com esta placa");
        }
        return veiculoRepository.save(veiculo);
    }

    public VeiculoModel atualizar(UUID id, VeiculoModel dados) {
        VeiculoModel veiculo = buscarPorId(id);
        if (!veiculo.getPlaca().equals(dados.getPlaca())
                && veiculoRepository.existsByPlaca(dados.getPlaca())) {
            throw new RuntimeException("Já existe um veículo cadastrado com esta placa");
        }
        veiculo.setPlaca(dados.getPlaca());
        veiculo.setMarca(dados.getMarca());
        veiculo.setModeloBase(dados.getModeloBase());
        veiculo.setCategoria(dados.getCategoria());
        veiculo.setEspecificacaoMotor(dados.getEspecificacaoMotor());
        veiculo.setAnoFabricacao(dados.getAnoFabricacao());
        return veiculoRepository.save(veiculo);
    }

    public void excluir(UUID id) {
        veiculoRepository.delete(buscarPorId(id));
    }

    public BigDecimal calcularGastos(UUID id, LocalDate inicio, LocalDate fim) {
        return buscarPorId(id).calcularGastosTotais(inicio, fim);
    }
}

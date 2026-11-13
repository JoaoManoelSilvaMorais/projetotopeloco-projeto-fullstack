package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@Service
@Transactional
public class VeiculoService {
    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public List<VeiculoModel> listarTodos() {
        return veiculoRepository.findAll();
    }

    public VeiculoModel buscarPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do veículo não pode ser nulo");
        }
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
    }

    public VeiculoModel criar(VeiculoModel veiculo) {
        validarVeiculo(veiculo);
        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new RuntimeException("Já existe um veículo cadastrado com esta placa");
        }
        return veiculoRepository.save(veiculo);
    }

    public VeiculoModel atualizar(UUID id, VeiculoModel dados) {
        validarVeiculo(dados);
        VeiculoModel veiculo = buscarPorId(id);
        if (!veiculo.getPlaca().equalsIgnoreCase(dados.getPlaca())
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
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new IllegalArgumentException("A data de início não pode ser posterior à data de fim");
        }
        return buscarPorId(id).calcularGastosTotais(inicio, fim);
    }

    private void validarVeiculo(VeiculoModel veiculo) {
        if (veiculo == null) {
            throw new IllegalArgumentException("Dados do veículo são obrigatórios");
        }
        if (veiculo.getPlaca() == null || veiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }
        if (veiculo.getModeloBase() == null || veiculo.getModeloBase().trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo é obrigatório");
        }
        if (veiculo.getCategoria() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        if (veiculo.getAnoFabricacao() == null) {
            throw new IllegalArgumentException("Ano de fabricação é obrigatório");
        }
        if (veiculo.getAnoFabricacao() <= 0) {
            throw new IllegalArgumentException("Ano de fabricação deve ser positivo");
        }
    }
}

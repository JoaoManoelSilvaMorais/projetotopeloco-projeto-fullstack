package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "http://localhost:5173")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping
    public List<VeiculoModel> listar() {
        return veiculoRepository.findAll();
    }

    @GetMapping("/{id}")
    public VeiculoModel buscarPorId(@PathVariable UUID id) {
        return buscarVeiculo(id);
    }

    @GetMapping("/{id}/gastos")
    public BigDecimal calcularGastos(
            @PathVariable UUID id,
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim) {
        return buscarVeiculo(id).calcularGastosTotais(inicio, fim);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoModel criar(@RequestBody VeiculoModel veiculo) {
        return veiculoRepository.save(veiculo);
    }

    @PutMapping("/{id}")
    public VeiculoModel atualizar(@PathVariable UUID id, @RequestBody VeiculoModel dados) {
        VeiculoModel veiculo = buscarVeiculo(id);
        veiculo.setPlaca(dados.getPlaca());
        veiculo.setMarca(dados.getMarca());
        veiculo.setModeloBase(dados.getModeloBase());
        veiculo.setCategoria(dados.getCategoria());
        veiculo.setEspecificacaoMotor(dados.getEspecificacaoMotor());
        veiculo.setAnoFabricacao(dados.getAnoFabricacao());
        return veiculoRepository.save(veiculo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        veiculoRepository.delete(buscarVeiculo(id));
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
    }
}

package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Controller;

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

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.DespesaRecorrenteRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@RestController
@RequestMapping("/api/despesas-recorrentes")
@CrossOrigin(origins = "http://localhost:5173")
public class DespesaRecorrenteController {

    private final DespesaRecorrenteRepository despesaRepository;
    private final VeiculoRepository veiculoRepository;

    public DespesaRecorrenteController(DespesaRecorrenteRepository despesaRepository,
            VeiculoRepository veiculoRepository) {
        this.despesaRepository = despesaRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping
    public List<DespesaRecorrenteModel> listar(@RequestParam(required = false) UUID veiculoId) {
        if (veiculoId == null) {
            return despesaRepository.findAll();
        }
        return despesaRepository.findAll().stream()
                .filter(despesa -> despesa.getVeiculo() != null
                        && veiculoId.equals(despesa.getVeiculo().getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public DespesaRecorrenteModel buscarPorId(@PathVariable UUID id) {
        return buscarDespesa(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaRecorrenteModel criar(@RequestBody DespesaRecorrenteModel despesa,
            @RequestParam UUID veiculoId) {
        despesa.setVeiculo(buscarVeiculo(veiculoId));
        return despesaRepository.save(despesa);
    }

    @PutMapping("/{id}")
    public DespesaRecorrenteModel atualizar(@PathVariable UUID id,
            @RequestBody DespesaRecorrenteModel dados,
            @RequestParam UUID veiculoId) {
        DespesaRecorrenteModel despesa = buscarDespesa(id);
        despesa.setTipo(dados.getTipo());
        despesa.setValorMensal(dados.getValorMensal());
        despesa.setDiaVencimento(dados.getDiaVencimento());
        despesa.setStatusAtivo(dados.getStatusAtivo());
        despesa.setVeiculo(buscarVeiculo(veiculoId));
        return despesaRepository.save(despesa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        despesaRepository.delete(buscarDespesa(id));
    }

    private DespesaRecorrenteModel buscarDespesa(UUID id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despesa não encontrada"));
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
    }
}

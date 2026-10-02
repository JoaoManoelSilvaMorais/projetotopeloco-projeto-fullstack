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

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.DespesaRecorrenteService;

@RestController
@RequestMapping("/api/despesas-recorrentes")
@CrossOrigin(origins = "http://localhost:5173")
public class DespesaRecorrenteController {

    private final DespesaRecorrenteService despesaService;

    public DespesaRecorrenteController(DespesaRecorrenteService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public List<DespesaRecorrenteModel> listar(@RequestParam(required = false) UUID veiculoId) {
        if (veiculoId == null) {
            return despesaService.listarTodos();
        }
        return despesaService.listarPorVeiculo(veiculoId);
    }

    @GetMapping("/{id}")
    public DespesaRecorrenteModel buscarPorId(@PathVariable UUID id) {
        return despesaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaRecorrenteModel criar(@RequestBody DespesaRecorrenteModel despesa,
            @RequestParam UUID veiculoId) {
        return despesaService.criar(despesa, veiculoId);
    }

    @PutMapping("/{id}")
    public DespesaRecorrenteModel atualizar(@PathVariable UUID id,
            @RequestBody DespesaRecorrenteModel dados,
            @RequestParam UUID veiculoId) {
        return despesaService.atualizar(id, dados, veiculoId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        despesaService.excluir(id);
    }
}

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

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.ServicoManutencaoService;

@RestController
@RequestMapping("/api/servicos-manutencao")
@CrossOrigin(origins = "http://localhost:5173")
public class ServicoManutencaoController {

    private final ServicoManutencaoService servicoService;

    public ServicoManutencaoController(ServicoManutencaoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public List<ServicoManutencaoModel> listar(@RequestParam(required = false) UUID veiculoId) {
        if (veiculoId == null) {
            return servicoService.listarTodos();
        }
        return servicoService.listarPorVeiculo(veiculoId);
    }

    @GetMapping("/{id}")
    public ServicoManutencaoModel buscarPorId(@PathVariable UUID id) {
        return servicoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoManutencaoModel criar(@RequestBody ServicoManutencaoModel servico,
            @RequestParam UUID veiculoId) {
        return servicoService.criar(servico, veiculoId);
    }

    @PutMapping("/{id}")
    public ServicoManutencaoModel atualizar(@PathVariable UUID id,
            @RequestBody ServicoManutencaoModel dados,
            @RequestParam UUID veiculoId) {
        return servicoService.atualizar(id, dados, veiculoId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        servicoService.excluir(id);
    }
}

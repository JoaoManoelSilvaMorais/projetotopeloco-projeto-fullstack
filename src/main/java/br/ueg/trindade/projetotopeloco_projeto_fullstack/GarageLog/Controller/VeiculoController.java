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

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.VeiculoService;

@RestController
@RequestMapping("/api/veiculos")
@CrossOrigin(origins = "http://localhost:5173")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public List<VeiculoModel> listar() {
        return veiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public VeiculoModel buscarPorId(@PathVariable UUID id) {
        return veiculoService.buscarPorId(id);
    }

    @GetMapping("/{id}/gastos")
    public BigDecimal calcularGastos(@PathVariable UUID id,
            @RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return veiculoService.calcularGastos(id, inicio, fim);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoModel criar(@RequestBody VeiculoModel veiculo) {
        return veiculoService.criar(veiculo);
    }

    @PutMapping("/{id}")
    public VeiculoModel atualizar(@PathVariable UUID id, @RequestBody VeiculoModel dados) {
        return veiculoService.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        veiculoService.excluir(id);
    }
}

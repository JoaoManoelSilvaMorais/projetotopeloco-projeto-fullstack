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

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.VeiculoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.ServicoManutencaoRepository;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.VeiculoRepository;

@RestController
@RequestMapping("/api/servicos-manutencao")
@CrossOrigin(origins = "http://localhost:5173")
public class ServicoManutencaoController {

    private final ServicoManutencaoRepository servicoRepository;
    private final VeiculoRepository veiculoRepository;

    public ServicoManutencaoController(ServicoManutencaoRepository servicoRepository,
            VeiculoRepository veiculoRepository) {
        this.servicoRepository = servicoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @GetMapping
    public List<ServicoManutencaoModel> listar(@RequestParam(required = false) UUID veiculoId) {
        if (veiculoId == null) {
            return servicoRepository.findAll();
        }
        return servicoRepository.findAll().stream()
                .filter(servico -> servico.getVeiculo() != null
                        && veiculoId.equals(servico.getVeiculo().getId()))
                .toList();
    }

    @GetMapping("/{id}")
    public ServicoManutencaoModel buscarPorId(@PathVariable UUID id) {
        return buscarServico(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoManutencaoModel criar(@RequestBody ServicoManutencaoModel servico,
            @RequestParam UUID veiculoId) {
        VeiculoModel veiculo = buscarVeiculo(veiculoId);
        servico.setVeiculo(veiculo);
        return servicoRepository.save(servico);
    }

    @PutMapping("/{id}")
    public ServicoManutencaoModel atualizar(@PathVariable UUID id,
            @RequestBody ServicoManutencaoModel dados,
            @RequestParam UUID veiculoId) {
        ServicoManutencaoModel servico = buscarServico(id);
        servico.setTipoIntervencao(dados.getTipoIntervencao());
        servico.setDescricao(dados.getDescricao());
        servico.setDataRealizacao(dados.getDataRealizacao());
        servico.setKmAtual(dados.getKmAtual());
        servico.setProximaRevisaoKm(dados.getProximaRevisaoKm());
        servico.setCustoPecas(dados.getCustoPecas());
        servico.setCustoMaoDeObra(dados.getCustoMaoDeObra());
        servico.setVeiculo(buscarVeiculo(veiculoId));
        return servicoRepository.save(servico);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable UUID id) {
        servicoRepository.delete(buscarServico(id));
    }

    private ServicoManutencaoModel buscarServico(UUID id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado"));
    }

    private VeiculoModel buscarVeiculo(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
    }
}

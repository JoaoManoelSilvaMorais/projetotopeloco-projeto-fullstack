package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository.PermissaoRepository;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class PermissaoController {
    @Autowired
	private PermissaoRepository permissaoRepository;

	PermissaoController(PermissaoRepository permissaoRepository) {
		this.permissaoRepository = permissaoRepository;
	}

	@GetMapping("/permissao")
	public List<PermissaoModel> getAllPermissao() {
		return permissaoRepository.findAll();
	}

	@GetMapping("/permissao/{id}")
	public PermissaoModel getPermissaoById(@PathVariable Long id) {
		return permissaoRepository.findById(id)

				.orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

	}

	@DeleteMapping("/permissao/{id}")
	public void deleteUsuario(@PathVariable Long id) {
		permissaoRepository.deleteById(id);
	}

	@PostMapping("/permissao")
	public PermissaoModel createPermissao(@RequestBody PermissaoModel permissao) {
		return permissaoRepository.save(permissao);
	}

	@PutMapping("/permissao/{id}")
	public PermissaoModel updatePermissao(@PathVariable Long id, @RequestBody PermissaoModel permissaoAtualizada) {
		PermissaoModel permissao = permissaoRepository.findById(id)

				.orElseThrow(() -> new RuntimeException("Permissão não encontrada"));

		permissao.setNome(permissaoAtualizada.getNome());
		permissao.setDescricao(permissaoAtualizada.getDescricao());
		return permissaoRepository.save(permissao);
	}
}

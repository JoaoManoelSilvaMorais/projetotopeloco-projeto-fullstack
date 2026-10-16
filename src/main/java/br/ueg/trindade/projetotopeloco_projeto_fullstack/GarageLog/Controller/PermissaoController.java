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
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.PermissaoService;


@RestController
@RequestMapping("/api/permissoes")
@CrossOrigin(origins = "http://localhost:5173")
public class PermissaoController {
	@Autowired
	private final PermissaoService permissaoService;

	public PermissaoController(PermissaoService permissaoService) {
		this.permissaoService = permissaoService;
	}

	@GetMapping
	public List<PermissaoModel> listarTodos() {
		return permissaoService.listarTodos();
	}

	@GetMapping("/{id}")
	public PermissaoModel buscarPorId(@PathVariable Long id) {
		return permissaoService.buscarPorId(id);
	}

	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		permissaoService.excluir(id);
	}

	@PostMapping
	public PermissaoModel criar(@RequestBody PermissaoModel permissao) {
		return permissaoService.criar(permissao);
	}

	@PutMapping("/{id}")
	public PermissaoModel atualizar(@PathVariable Long id, @RequestBody PermissaoModel permissao) {
		return permissaoService.atualizar(id, permissao);
	}
}

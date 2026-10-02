package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;
import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {
	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public List<UsuarioModel> listarTodos() {
		return usuarioService.listarTodos();
	}

	@GetMapping("/{id}")
	public UsuarioModel buscarPorId(@PathVariable Long id) {
		return usuarioService.buscarPorId(id);
	}

	@DeleteMapping("/{id}")
	public void excluir(@PathVariable Long id) {
		usuarioService.excluir(id);
	}

	@PostMapping
	public UsuarioModel criar(@RequestBody UsuarioModel usuario) {
		return usuarioService.criar(usuario);
	}

	@PutMapping("/{id}")
	public UsuarioModel atualizar(@PathVariable Long id, @RequestBody UsuarioModel usuario) {
		return usuarioService.atualizar(id, usuario);
	}
}
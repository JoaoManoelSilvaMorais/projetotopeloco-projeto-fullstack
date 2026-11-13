package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@GetMapping
	public List<UsuarioModel> listarTodos() {
		return usuarioService.listarTodos();
	}

	@GetMapping("/senha-padrao")
	public Map<String, String> obterSenhaPadrao() {
		return Map.of("senhaPadrao", UsuarioService.SENHA_PADRAO);
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

	@GetMapping("/{id}/permissoes")
	public UsuarioModel buscarPermissoes(@PathVariable Long id) {
		return usuarioService.buscarPorId(id);
	}

	@PutMapping("/{id}/permissoes")
	public UsuarioModel atualizarPermissoes(
			@PathVariable Long id,
			@RequestBody List<Long> idsPermissoes) {
		return usuarioService.atribuirPermissoes(id, idsPermissoes);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Map.of("mensagem", ex.getMessage()));
	}
}

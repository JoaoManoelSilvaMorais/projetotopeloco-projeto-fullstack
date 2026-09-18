package br.ueg.trindade.projetotopeloco_projeto_fullstack;

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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {
	@Autowired
	private UsuarioRepository usuarioRepository;

	UsuarioController(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@GetMapping("/usuarios")
	public List<UsuarioModel> getAllUsuarios() {
		return usuarioRepository.findAll();
	}

	@GetMapping("/usuarios/{id}")
	public UsuarioModel getUsuarioById(@PathVariable Long id) {
		return usuarioRepository.findById(id)

				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

	}

	@DeleteMapping("/usuarios/{id}")
	public void deleteUsuario(@PathVariable Long id) {
		usuarioRepository.deleteById(id);
	}

	@PostMapping("/usuarios")
	public UsuarioModel createUsuario(@RequestBody UsuarioModel usuario) {
		return usuarioRepository.save(usuario);
	}

	// UPDATE
	@PutMapping("/usuarios/{id}")
	public UsuarioModel updateUsuario(@PathVariable Long id, @RequestBody UsuarioModel usuarioAtualizado) {
		UsuarioModel usuario = usuarioRepository.findById(id)

				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

		usuario.setNome(usuarioAtualizado.getNome());
		usuario.setUsername(usuarioAtualizado.getUsername());
		usuario.setEmail(usuarioAtualizado.getEmail());
		return usuarioRepository.save(usuario);
	}
}
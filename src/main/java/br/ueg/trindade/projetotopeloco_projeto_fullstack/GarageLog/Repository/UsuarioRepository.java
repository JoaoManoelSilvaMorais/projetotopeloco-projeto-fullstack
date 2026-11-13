package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.UsuarioModel;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
	Optional<UsuarioModel> findByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	boolean existsByEmailAndIdNot(String email, Long id);
	boolean existsByUsernameAndIdNot(String username, Long id);
}
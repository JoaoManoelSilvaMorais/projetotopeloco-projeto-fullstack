package br.ueg.trindade.projetotopeloco_projeto_fullstack;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
}

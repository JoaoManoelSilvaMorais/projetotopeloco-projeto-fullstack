package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.PermissaoModel;

public interface PermissaoRepository extends JpaRepository<PermissaoModel, Long> {
	Optional<PermissaoModel> findByNome(String nome);
	boolean existsByNome(String nome);
}

package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.ServicoManutencaoModel;

public interface ServicoManutencaoRepository extends JpaRepository<ServicoManutencaoModel, UUID> {
}

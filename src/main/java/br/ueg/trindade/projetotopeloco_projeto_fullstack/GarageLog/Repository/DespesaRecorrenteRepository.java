package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model.DespesaRecorrenteModel;

public interface DespesaRecorrenteRepository extends JpaRepository<DespesaRecorrenteModel, UUID> {
}

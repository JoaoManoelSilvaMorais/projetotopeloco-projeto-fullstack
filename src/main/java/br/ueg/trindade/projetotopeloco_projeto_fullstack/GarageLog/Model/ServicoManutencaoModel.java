package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.TipoServico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ServicoManutencaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private TipoServico tipoIntervencao;
    private String descricao;
    private LocalDate dataRealizacao;
    private Integer kmAtual;
    private Integer proximaRevisaoKm;
    private BigDecimal custoPecas;
    private BigDecimal custoMaoDeObra;

    @ManyToOne
    @JsonIgnore
    private VeiculoModel veiculo;

    public ServicoManutencaoModel() {
    }

    public ServicoManutencaoModel(TipoServico tipoIntervencao, String descricao, LocalDate dataRealizacao,
            Integer kmAtual, Integer proximaRevisaoKm, BigDecimal custoPecas, BigDecimal custoMaoDeObra,
            VeiculoModel veiculo) {
        this.tipoIntervencao = tipoIntervencao;
        this.descricao = descricao;
        this.dataRealizacao = dataRealizacao;
        this.kmAtual = kmAtual;
        this.proximaRevisaoKm = proximaRevisaoKm;
        this.custoPecas = custoPecas;
        this.custoMaoDeObra = custoMaoDeObra;
        this.veiculo = veiculo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TipoServico getTipoIntervencao() {
        return tipoIntervencao;
    }

    public void setTipoIntervencao(TipoServico tipoIntervencao) {
        this.tipoIntervencao = tipoIntervencao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate dataRealizacao) {
        this.dataRealizacao = dataRealizacao;
    }

    public Integer getKmAtual() {
        return kmAtual;
    }

    public void setKmAtual(Integer kmAtual) {
        this.kmAtual = kmAtual;
    }

    public Integer getProximaRevisaoKm() {
        return proximaRevisaoKm;
    }

    public void setProximaRevisaoKm(Integer proximaRevisaoKm) {
        this.proximaRevisaoKm = proximaRevisaoKm;
    }

    public BigDecimal getCustoPecas() {
        return custoPecas;
    }

    public void setCustoPecas(BigDecimal custoPecas) {
        this.custoPecas = custoPecas;
    }

    public BigDecimal getCustoMaoDeObra() {
        return custoMaoDeObra;
    }

    public void setCustoMaoDeObra(BigDecimal custoMaoDeObra) {
        this.custoMaoDeObra = custoMaoDeObra;
    }

    public VeiculoModel getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(VeiculoModel veiculo) {
        this.veiculo = veiculo;
    }

    public BigDecimal calcularCustoTotal() {
        return (custoPecas == null ? BigDecimal.ZERO : custoPecas)
                .add(custoMaoDeObra == null ? BigDecimal.ZERO : custoMaoDeObra);
    }

    public Boolean verificarAlertaRevisao(Integer kmVeiculoAtual) {
        if (proximaRevisaoKm != null && kmVeiculoAtual != null) {
            return kmVeiculoAtual >= proximaRevisaoKm;
        }
        return false;
    }

}

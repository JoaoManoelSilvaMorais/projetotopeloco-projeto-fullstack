package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import java.math.BigDecimal;
import java.util.UUID;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.TipoDespesa;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity 
public class DespesaRecorrenteModel {

    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    private TipoDespesa tipo;

    private BigDecimal valorMensal;
    private Integer diaVencimento;
    private Boolean statusAtivo;
    
    @ManyToOne
    @JsonIgnore
    private VeiculoModel veiculo;
    
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public TipoDespesa getTipo() {
        return tipo;
    }
    public void setTipo(TipoDespesa tipo) {
        this.tipo = tipo;
    }
    public BigDecimal getValorMensal() {
        return valorMensal;
    }
    public void setValorMensal(BigDecimal valorMensal) {
        this.valorMensal = valorMensal;
    }
    public Integer getDiaVencimento() {
        return diaVencimento;
    }
    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }
    public Boolean getStatusAtivo() {
        return statusAtivo;
    }
    public void setStatusAtivo(Boolean statusAtivo) {
        this.statusAtivo = statusAtivo;
    }
    public VeiculoModel getVeiculo() {
        return veiculo;
    }
    public void setVeiculo(VeiculoModel veiculo) {
        this.veiculo = veiculo;
    }

    public void processarPagamento(){

    }

    
}

package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum.CategoriaVeiculo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class VeiculoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String placa;
    private String marca;
    private String modeloBase;

    @Enumerated(EnumType.STRING)
    private CategoriaVeiculo categoria;

    private String especificacaoMotor;
    private Integer anoFabricacao;

    @OneToMany(mappedBy = "veiculo", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<ServicoManutencaoModel> historicoServicos = new ArrayList<>();

    @OneToMany(mappedBy = "veiculo", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<DespesaRecorrenteModel> despesas = new ArrayList<>();

    public VeiculoModel() {
    }

    public VeiculoModel(String placa, String marca, String modeloBase, CategoriaVeiculo categoria,
            String especificacaoMotor, Integer anoFabricacao) {
        this.placa = placa;
        this.marca = marca;
        this.modeloBase = modeloBase;
        this.categoria = categoria;
        this.especificacaoMotor = especificacaoMotor;
        this.anoFabricacao = anoFabricacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModeloBase() {
        return modeloBase;
    }

    public void setModeloBase(String modeloBase) {
        this.modeloBase = modeloBase;
    }

    public CategoriaVeiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVeiculo categoria) {
        this.categoria = categoria;
    }

    public String getEspecificacaoMotor() {
        return especificacaoMotor;
    }

    public void setEspecificacaoMotor(String especificacaoMotor) {
        this.especificacaoMotor = especificacaoMotor;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public List<ServicoManutencaoModel> getHistoricoServicos() {
        return historicoServicos;
    }

    public void adicionarServico(ServicoManutencaoModel servico) {
        if (servico != null && !historicoServicos.contains(servico)) {
            historicoServicos.add(servico);
            servico.setVeiculo(this);
        }
    }

    public List<DespesaRecorrenteModel> getDespesas() {
        return despesas;
    }

    public void adicionarDespesa(DespesaRecorrenteModel despesa) {
        if (despesa != null && !despesas.contains(despesa)) {
            despesas.add(despesa);
            despesa.setVeiculo(this);
        }
    }

    public BigDecimal calcularGastosTotais(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (ServicoManutencaoModel servico : historicoServicos) {
            LocalDate dataRealizacao = servico.getDataRealizacao();
            if (dataRealizacao != null
                    && !dataRealizacao.isBefore(inicio)
                    && !dataRealizacao.isAfter(fim)) {
                total = total.add(servico.calcularCustoTotal());
            }
        }

        for (DespesaRecorrenteModel despesa : despesas) {
            if (!Boolean.TRUE.equals(despesa.getStatusAtivo())
                    || despesa.getValorMensal() == null
                    || despesa.getDiaVencimento() == null
                    || despesa.getDiaVencimento() < 1
                    || despesa.getDiaVencimento() > 31) {
                continue;
            }

            YearMonth mes = YearMonth.from(inicio);
            YearMonth ultimoMes = YearMonth.from(fim);
            while (!mes.isAfter(ultimoMes)) {
                int diaVencimento = despesa.getDiaVencimento();
                if (diaVencimento <= mes.lengthOfMonth()) {
                    LocalDate vencimento = mes.atDay(diaVencimento);
                    if (!vencimento.isBefore(inicio) && !vencimento.isAfter(fim)) {
                        total = total.add(despesa.getValorMensal());
                    }
                }
                mes = mes.plusMonths(1);
            }
        }

        return total;
    }

}

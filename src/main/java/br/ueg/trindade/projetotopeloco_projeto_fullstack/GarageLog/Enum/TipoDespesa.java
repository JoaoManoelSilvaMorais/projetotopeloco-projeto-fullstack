package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum;

public enum TipoDespesa {
    PROTECAO_VEICULAR("Proteção Veicular"),
    IMPOSTO("Imposto"),
    ASSISTENCIA_24H("Assistência 24h");

    private final String descricao;

    TipoDespesa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}

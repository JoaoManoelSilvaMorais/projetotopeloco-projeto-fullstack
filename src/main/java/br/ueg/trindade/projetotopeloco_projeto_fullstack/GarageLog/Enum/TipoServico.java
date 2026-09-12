package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum;

public enum TipoServico {
    MECANICA_PREVENTIVA("Mecânica Preventiva"),
    MECANICA_CORRETIVA("Mecânica Corretiva"),
    UPGRADE_PERFORMANCE("Upgrade de Performance");

    private final String descricao;

    TipoServico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

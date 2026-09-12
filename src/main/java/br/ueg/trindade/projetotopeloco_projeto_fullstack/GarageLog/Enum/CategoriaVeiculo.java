//Categorias: CONVENCIONAL, CLASSICO, MODIFICADO.

package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Enum;

public enum CategoriaVeiculo {
    
    CONVENCIONAL("Convencional"),
    CLASSICO("Clássico"),
    MODIFICADO("Modificado");

    private final String descricao;

    CategoriaVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

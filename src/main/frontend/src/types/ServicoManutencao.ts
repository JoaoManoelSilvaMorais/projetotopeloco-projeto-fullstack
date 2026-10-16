export interface ServicoManutencao {
  id: string;
  tipoIntervencao: string;
  descricao: string;
  dataRealizacao: string;
  kmAtual: number;
  proximaRevisaoKm: number;
  custoPecas: number | null;
  custoMaoDeObra: number | null;
}

import type { Veiculo } from "./Veiculo";

export interface ServicoManutencao {
  id: string;
  tipoIntervencao: string;
  tipoServico?: string;
  descricao: string;
  dataRealizacao: string;
  data?: string;
  kmAtual: number;
  proximaRevisaoKm: number;
  custoPecas: number | null;
  custoMaoDeObra: number | null;
  valor?: number | null;
  veiculo?: Veiculo;
}

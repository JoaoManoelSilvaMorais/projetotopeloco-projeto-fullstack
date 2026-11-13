import type { Veiculo } from "./Veiculo";

export interface DespesaRecorrente {
  id: string;
  tipo: string;
  tipoDespesa?: string;
  descricao?: string;
  valorMensal: number;
  valor?: number;
  diaVencimento: number;
  dataVencimento?: string | number;
  statusAtivo: boolean;
  veiculo?: Veiculo;
}

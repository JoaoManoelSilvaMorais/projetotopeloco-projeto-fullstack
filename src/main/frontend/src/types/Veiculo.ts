export interface Veiculo {
  id: string;
  placa: string;
  marca: string;
  modeloBase: string;
  categoria: string;
  especificacaoMotor: string;
  anoFabricacao: number;
}

export type { DespesaRecorrente } from "./DespesaRecorrente";
export type { ServicoManutencao } from "./ServicoManutencao";
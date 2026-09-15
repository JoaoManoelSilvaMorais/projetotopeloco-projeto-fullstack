export interface Veiculo {
    id: string;
    placa: string;
    marca: string;
    modeloBase: string;
    categoria: string;
    especificacaoMotor: string;
    anoFabricacao: number;
}

export interface DespesaRecorrente {
    id: string;
    tipo: string;
    valorMensal: number;
    diaVencimento: number;
    statusAtivo: boolean;
}

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
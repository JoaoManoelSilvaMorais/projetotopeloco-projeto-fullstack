import type { ServicoManutencao } from "../types/ServicoManutencao";

interface ServicoItemProps {
  servico: ServicoManutencao;
}

function ServicoItem({ servico }: ServicoItemProps) {
  return <span>{servico.tipoIntervencao} - {servico.descricao} ({servico.dataRealizacao})</span>;
}

export default ServicoItem;

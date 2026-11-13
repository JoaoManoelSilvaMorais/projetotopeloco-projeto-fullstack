import { Link } from "react-router-dom";
import type { ServicoManutencao } from "../types/ServicoManutencao";

interface ServicoItemProps {
  servico: ServicoManutencao;
}

function ServicoItem({ servico }: ServicoItemProps) {
  const tipo = servico.tipoIntervencao || servico.tipoServico || "Serviço";
  const data = servico.dataRealizacao || servico.data || "";

  return (
    <span>
      <Link
        to={`/servicos/${servico.id}`}
        className="font-bold text-blue-600 hover:text-blue-800 hover:underline mr-1"
      >
        {tipo} - {servico.descricao}
      </Link>
      {data && <span className="text-gray-600">({data})</span>}
    </span>
  );
}

export default ServicoItem;

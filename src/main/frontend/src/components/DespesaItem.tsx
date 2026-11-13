import { Link } from "react-router-dom";
import type { DespesaRecorrente } from "../types/DespesaRecorrente";

interface DespesaItemProps {
  despesa: DespesaRecorrente;
}

function DespesaItem({ despesa }: DespesaItemProps) {
  const tipo = despesa.tipo || despesa.tipoDespesa || "Despesa";
  const valor = despesa.valorMensal ?? despesa.valor ?? 0;
  const vencimento = despesa.diaVencimento ?? despesa.dataVencimento ?? "";

  return (
    <span>
      <Link
        to={`/despesas/${despesa.id}`}
        className="font-bold text-blue-600 hover:text-blue-800 hover:underline mr-1"
      >
        {tipo}
      </Link>
      : R$ {Number(valor).toFixed(2)} - vencimento dia {vencimento}
    </span>
  );
}

export default DespesaItem;

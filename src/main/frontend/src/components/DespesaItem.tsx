import type { DespesaRecorrente } from "../types/DespesaRecorrente";

interface DespesaItemProps {
  despesa: DespesaRecorrente;
}

function DespesaItem({ despesa }: DespesaItemProps) {
  return <span>{despesa.tipo}: R$ {despesa.valorMensal} - vencimento dia {despesa.diaVencimento}</span>;
}

export default DespesaItem;

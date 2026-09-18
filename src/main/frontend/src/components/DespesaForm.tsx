import { useState, type FormEvent } from "react";
import api from "../services/api";
import type { DespesaRecorrente } from "../types/Veiculo";

interface DespesaFormProps {
  veiculoId: string;
  despesaEditando?: DespesaRecorrente | null;
  onDespesaSalva: () => void;
}

const tipos = ["PROTECAO_VEICULAR", "IMPOSTO", "ASSISTENCIA_24H"];

function DespesaForm({ veiculoId, despesaEditando, onDespesaSalva }: DespesaFormProps) {
  const [tipo, setTipo] = useState(despesaEditando?.tipo ?? "PROTECAO_VEICULAR");
  const [valorMensal, setValorMensal] = useState(despesaEditando?.valorMensal?.toString() ?? "");
  const [diaVencimento, setDiaVencimento] = useState(despesaEditando?.diaVencimento?.toString() ?? "");
  const [statusAtivo, setStatusAtivo] = useState(despesaEditando?.statusAtivo ?? true);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const dados = {
      tipo,
      valorMensal: Number(valorMensal),
      diaVencimento: Number(diaVencimento),
      statusAtivo,
    };
    const config = { params: { veiculoId } };

    if (despesaEditando) {
      await api.put(`/despesas-recorrentes/${despesaEditando.id}`, dados, config);
    } else {
      await api.post("/despesas-recorrentes", dados, config);
    }

    onDespesaSalva();
  }

  return (
    <form onSubmit={handleSubmit}>
      <select value={tipo} onChange={(event) => setTipo(event.target.value)}>
        {tipos.map((opcao) => <option key={opcao} value={opcao}>{opcao}</option>)}
      </select>
      <input type="number" min="0" step="0.01" value={valorMensal} onChange={(event) => setValorMensal(event.target.value)} placeholder="Valor mensal" required />
      <input type="number" min="1" max="31" value={diaVencimento} onChange={(event) => setDiaVencimento(event.target.value)} placeholder="Dia de vencimento" required />
      <label>
        <input type="checkbox" checked={statusAtivo} onChange={(event) => setStatusAtivo(event.target.checked)} />
        Ativa
      </label>
      <button type="submit">{despesaEditando ? "Salvar despesa" : "Adicionar despesa"}</button>
    </form>
  );
}

export default DespesaForm;

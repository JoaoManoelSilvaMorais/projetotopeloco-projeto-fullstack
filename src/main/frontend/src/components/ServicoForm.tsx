import { useState, type FormEvent } from "react";
import type { ServicoManutencao } from "../types/ServicoManutencao";

interface ServicoFormProps {
  servicoEditando?: ServicoManutencao | null;
  onServicoSalvo: (dados: Omit<ServicoManutencao, "id">) => void | Promise<void>;
}

const tipos = ["MECANICA_PREVENTIVA", "MECANICA_CORRETIVA", "UPGRADE_PERFORMANCE"];

function ServicoForm({ servicoEditando, onServicoSalvo }: ServicoFormProps) {
  const [tipoIntervencao, setTipoIntervencao] = useState(servicoEditando?.tipoIntervencao ?? "MECANICA_PREVENTIVA");
  const [descricao, setDescricao] = useState(servicoEditando?.descricao ?? "");
  const [dataRealizacao, setDataRealizacao] = useState(servicoEditando?.dataRealizacao ?? "");
  const [kmAtual, setKmAtual] = useState(servicoEditando?.kmAtual?.toString() ?? "");
  const [proximaRevisaoKm, setProximaRevisaoKm] = useState(servicoEditando?.proximaRevisaoKm?.toString() ?? "");
  const [custoPecas, setCustoPecas] = useState(servicoEditando?.custoPecas?.toString() ?? "");
  const [custoMaoDeObra, setCustoMaoDeObra] = useState(servicoEditando?.custoMaoDeObra?.toString() ?? "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const dados = {
      tipoIntervencao,
      descricao,
      dataRealizacao,
      kmAtual: Number(kmAtual),
      proximaRevisaoKm: Number(proximaRevisaoKm),
      custoPecas: custoPecas ? Number(custoPecas) : null,
      custoMaoDeObra: custoMaoDeObra ? Number(custoMaoDeObra) : null,
    };
    await onServicoSalvo(dados);
  }

  return (
    <form onSubmit={handleSubmit}>
      <select value={tipoIntervencao} onChange={(event) => setTipoIntervencao(event.target.value)}>
        {tipos.map((opcao) => <option key={opcao} value={opcao}>{opcao}</option>)}
      </select>
      <input value={descricao} onChange={(event) => setDescricao(event.target.value)} placeholder="Descrição" required />
      <input type="date" value={dataRealizacao} onChange={(event) => setDataRealizacao(event.target.value)} required />
      <input type="number" min="0" value={kmAtual} onChange={(event) => setKmAtual(event.target.value)} placeholder="KM atual" required />
      <input type="number" min="0" value={proximaRevisaoKm} onChange={(event) => setProximaRevisaoKm(event.target.value)} placeholder="Próxima revisão (KM)" required />
      <input type="number" min="0" step="0.01" value={custoPecas} onChange={(event) => setCustoPecas(event.target.value)} placeholder="Custo das peças" />
      <input type="number" min="0" step="0.01" value={custoMaoDeObra} onChange={(event) => setCustoMaoDeObra(event.target.value)} placeholder="Custo da mão de obra" />
      <button type="submit">{servicoEditando ? "Salvar serviço" : "Adicionar serviço"}</button>
    </form>
  );
}

export default ServicoForm;

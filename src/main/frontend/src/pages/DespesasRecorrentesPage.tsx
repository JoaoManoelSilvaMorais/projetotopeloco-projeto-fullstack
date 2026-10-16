import { useEffect, useState } from "react";
import api from "../services/api";
import DespesaForm from "../components/DespesaForm";
import type { DespesaRecorrente } from "../types/DespesaRecorrente";
import type { Veiculo } from "../types/Veiculo";
import DespesaItem from "../components/DespesaItem";

function DespesasRecorrentesPage() {
  const [despesas, setDespesas] = useState<DespesaRecorrente[]>([]);
  const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
  const [veiculoId, setVeiculoId] = useState("");
  const [despesaEditando, setDespesaEditando] = useState<DespesaRecorrente | null>(null);

  async function carregarDespesas() {
    const resposta = await api.get<DespesaRecorrente[]>("/despesas-recorrentes", {
      params: veiculoId ? { veiculoId } : undefined,
    });
    setDespesas(resposta.data);
  }

  async function carregarVeiculos() {
    const resposta = await api.get<Veiculo[]>("/veiculos");
    setVeiculos(resposta.data);
  }

  useEffect(() => {
    void carregarVeiculos();
    void carregarDespesas();
  }, [veiculoId]);

  async function salvarDespesa(dados: Omit<DespesaRecorrente, "id">) {
    if (!veiculoId) {
      return;
    }

    const config = { params: { veiculoId } };
    if (despesaEditando) {
      await api.put(`/despesas-recorrentes/${despesaEditando.id}`, dados, config);
    } else {
      await api.post("/despesas-recorrentes", dados, config);
    }

    setDespesaEditando(null);
    await carregarDespesas();
  }

  async function excluirDespesa(id: string) {
    await api.delete(`/despesas-recorrentes/${id}`);
    await carregarDespesas();
  }

  return (
    <main>
      <h1>Despesas recorrentes</h1>
      <label>
        Veículo
        <select value={veiculoId} onChange={(event) => setVeiculoId(event.target.value)} required>
          <option value="">Selecione um veículo</option>
          {veiculos.map((veiculo) => (
            <option key={veiculo.id} value={veiculo.id}>
              {veiculo.placa} - {veiculo.marca} {veiculo.modeloBase}
            </option>
          ))}
        </select>
      </label>
      {veiculoId ? (
        <DespesaForm
          key={despesaEditando?.id ?? "nova"}
          despesaEditando={despesaEditando}
          onDespesaSalva={salvarDespesa}
        />
      ) : <p>Selecione um veículo para cadastrar uma despesa.</p>}
      <ul>
        {despesas.map((despesa) => (
          <li key={despesa.id}>
            <DespesaItem despesa={despesa} />
            <button type="button" onClick={() => setDespesaEditando(despesa)}>Editar</button>
            <button type="button" onClick={() => void excluirDespesa(despesa.id)}>Excluir</button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default DespesasRecorrentesPage;

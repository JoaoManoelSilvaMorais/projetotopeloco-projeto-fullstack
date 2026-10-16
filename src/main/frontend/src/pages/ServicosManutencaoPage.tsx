import { useEffect, useState } from "react";
import api from "../services/api";
import ServicoForm from "../components/ServicoForm";
import type { ServicoManutencao } from "../types/ServicoManutencao";
import type { Veiculo } from "../types/Veiculo";
import ServicoItem from "../components/ServicoItem";

function ServicosManutencaoPage() {
  const [servicos, setServicos] = useState<ServicoManutencao[]>([]);
  const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
  const [veiculoId, setVeiculoId] = useState("");
  const [servicoEditando, setServicoEditando] = useState<ServicoManutencao | null>(null);

  async function carregarServicos() {
    const resposta = await api.get<ServicoManutencao[]>("/servicos-manutencao", {
      params: veiculoId ? { veiculoId } : undefined,
    });
    setServicos(resposta.data);
  }

  async function carregarVeiculos() {
    const resposta = await api.get<Veiculo[]>("/veiculos");
    setVeiculos(resposta.data);
  }

  useEffect(() => {
    void carregarVeiculos();
    void carregarServicos();
  }, [veiculoId]);

  async function salvarServico(dados: Omit<ServicoManutencao, "id">) {
    if (!veiculoId) {
      return;
    }

    const config = { params: { veiculoId } };
    if (servicoEditando) {
      await api.put(`/servicos-manutencao/${servicoEditando.id}`, dados, config);
    } else {
      await api.post("/servicos-manutencao", dados, config);
    }

    setServicoEditando(null);
    await carregarServicos();
  }

  async function excluirServico(id: string) {
    await api.delete(`/servicos-manutencao/${id}`);
    await carregarServicos();
  }

  return (
    <main>
      <h1>Serviços de manutenção</h1>
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
        <ServicoForm
          key={servicoEditando?.id ?? "novo"}
          servicoEditando={servicoEditando}
          onServicoSalvo={salvarServico}
        />
      ) : <p>Selecione um veículo para cadastrar um serviço.</p>}
      <ul>
        {servicos.map((servico) => (
          <li key={servico.id}>
            <ServicoItem servico={servico} />
            <button type="button" onClick={() => setServicoEditando(servico)}>Editar</button>
            <button type="button" onClick={() => void excluirServico(servico.id)}>Excluir</button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default ServicosManutencaoPage;

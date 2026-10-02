import { useEffect, useState } from "react";
import api from "../services/api";
import VeiculoForm from "../components/VeiculoForm";
import type { Veiculo } from "../types/Veiculo";

function VeiculosPage() {
  const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
  const [veiculoEditando, setVeiculoEditando] = useState<Veiculo | null>(null);

  async function carregarVeiculos() {
    const resposta = await api.get<Veiculo[]>("/veiculos");
    setVeiculos(resposta.data);
  }

  useEffect(() => {
    void carregarVeiculos();
  }, []);

  async function salvarVeiculo(dados: Omit<Veiculo, "id">) {
    if (veiculoEditando) {
      await api.put(`/veiculos/${veiculoEditando.id}`, dados);
    } else {
      await api.post("/veiculos", dados);
    }

    setVeiculoEditando(null);
    await carregarVeiculos();
  }

  async function excluirVeiculo(id: string) {
    await api.delete(`/veiculos/${id}`);
    await carregarVeiculos();
  }

  return (
    <main>
      <h1>Veículos</h1>
      <VeiculoForm
        key={veiculoEditando?.id ?? "novo"}
        veiculoEditando={veiculoEditando}
        onVeiculoSalvo={salvarVeiculo}
      />
      <ul>
        {veiculos.map((veiculo) => (
          <li key={veiculo.id}>
            <strong>{veiculo.marca} {veiculo.modeloBase}</strong>
            {" "}({veiculo.placa}) - {veiculo.anoFabricacao}
            <button type="button" onClick={() => setVeiculoEditando(veiculo)}>
              Editar
            </button>
            <button type="button" onClick={() => void excluirVeiculo(veiculo.id)}>
              Excluir
            </button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default VeiculosPage;

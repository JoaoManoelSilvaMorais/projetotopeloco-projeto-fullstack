import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import type { Veiculo } from "../types/Veiculo";

function VeiculoDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [veiculo, setVeiculo] = useState<Veiculo | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    api.get<Veiculo>(`/veiculos/${id}`).then((response) => setVeiculo(response.data))
      .catch(() => setErro("Erro ao carregar detalhes do veículo."));
  }, [id]);

  if (erro) return <div><p style={{ color: "red" }}>{erro}</p><button onClick={() => navigate("/veiculos")}>Voltar</button></div>;
  if (!veiculo) return <p>Carregando...</p>;
  return <div><h1>{veiculo.marca} {veiculo.modeloBase}</h1><p><strong>Placa:</strong> {veiculo.placa}</p><p><strong>Ano:</strong> {veiculo.anoFabricacao}</p><p><strong>Categoria:</strong> {veiculo.categoria}</p><button onClick={() => navigate("/veiculos")}>Voltar</button></div>;
}

export default VeiculoDetailPage;

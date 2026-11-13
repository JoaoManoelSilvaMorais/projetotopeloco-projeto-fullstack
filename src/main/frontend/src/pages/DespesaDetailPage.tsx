import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import Botao from "../components/Botao";
import type { DespesaRecorrente } from "../types/DespesaRecorrente";

function DespesaDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [despesa, setDespesa] = useState<DespesaRecorrente | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    api
      .get<DespesaRecorrente>(`/despesas-recorrentes/${id}`)
      .then((response) => setDespesa(response.data))
      .catch(() => setErro("Erro ao carregar detalhes da despesa."));
  }, [id]);

  if (erro) {
    return (
      <div className="p-4">
        <p className="text-red-600 mb-4">{erro}</p>
        <Botao type="button" variante="neutro" onClick={() => navigate("/despesas")}>
          Voltar
        </Botao>
      </div>
    );
  }

  if (!despesa) {
    return <p className="p-4 text-gray-600">Carregando...</p>;
  }

  const tipoDespesa = despesa.tipo || despesa.tipoDespesa || "N/A";
  const valor = despesa.valorMensal ?? despesa.valor ?? 0;
  const dataVencimento = despesa.diaVencimento ?? despesa.dataVencimento ?? "N/A";
  const descricao = despesa.descricao || tipoDespesa;
  const veiculo = despesa.veiculo;

  return (
    <div className="p-6 max-w-2xl bg-white rounded-lg shadow-sm border border-gray-100">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">
        {descricao || "Detalhes da Despesa"}
      </h1>

      <div className="space-y-3 mb-6 text-gray-700">
        <p><strong>Descrição:</strong> {descricao}</p>
        <p><strong>Tipo de Despesa:</strong> {tipoDespesa}</p>
        <p><strong>Valor:</strong> R$ {Number(valor).toFixed(2)}</p>
        <p><strong>Data de Vencimento:</strong> Dia {dataVencimento}</p>
        {despesa.statusAtivo !== undefined && (
          <p><strong>Status:</strong> {despesa.statusAtivo ? "Ativa" : "Inativa"}</p>
        )}

        {veiculo && (
          <div className="mt-4 p-4 border border-gray-200 rounded-lg bg-gray-50">
            <h2 className="text-lg font-semibold text-gray-800 mb-2">Veículo Associado</h2>
            <p><strong>Placa:</strong> {veiculo.placa}</p>
            <p><strong>Marca:</strong> {veiculo.marca}</p>
            <p><strong>Modelo:</strong> {veiculo.modeloBase || (veiculo as { modelo?: string }).modelo}</p>
            {veiculo.anoFabricacao && (
              <p><strong>Ano:</strong> {veiculo.anoFabricacao}</p>
            )}
            {veiculo.categoria && (
              <p><strong>Categoria:</strong> {veiculo.categoria}</p>
            )}
          </div>
        )}
      </div>

      <Botao type="button" variante="neutro" onClick={() => navigate("/despesas")}>
        Voltar
      </Botao>
    </div>
  );
}

export default DespesaDetailPage;

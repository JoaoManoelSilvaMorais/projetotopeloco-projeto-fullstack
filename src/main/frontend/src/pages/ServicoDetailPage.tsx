import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import Botao from "../components/Botao";
import type { ServicoManutencao } from "../types/ServicoManutencao";

function ServicoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [servico, setServico] = useState<ServicoManutencao | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    api
      .get<ServicoManutencao>(`/servicos-manutencao/${id}`)
      .then((response) => setServico(response.data))
      .catch(() => setErro("Erro ao carregar detalhes do serviço."));
  }, [id]);

  if (erro) {
    return (
      <div className="p-4">
        <p className="text-red-600 mb-4">{erro}</p>
        <Botao type="button" variante="neutro" onClick={() => navigate("/servicos")}>
          Voltar
        </Botao>
      </div>
    );
  }

  if (!servico) {
    return <p className="p-4 text-gray-600">Carregando...</p>;
  }

  const tipoServico = servico.tipoIntervencao || servico.tipoServico || "N/A";
  const data = servico.dataRealizacao || servico.data || "N/A";
  const valor =
    servico.valor !== undefined && servico.valor !== null
      ? servico.valor
      : (servico.custoPecas ?? 0) + (servico.custoMaoDeObra ?? 0);
  const veiculo = servico.veiculo;

  return (
    <div className="p-6 max-w-2xl bg-white rounded-lg shadow-sm border border-gray-100">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">
        {servico.descricao || "Detalhes do Serviço"}
      </h1>

      <div className="space-y-3 mb-6 text-gray-700">
        <p><strong>Descrição:</strong> {servico.descricao}</p>
        <p><strong>Tipo de Serviço:</strong> {tipoServico}</p>
        <p><strong>Data:</strong> {data}</p>
        <p><strong>Valor:</strong> R$ {Number(valor).toFixed(2)}</p>

        {servico.custoPecas !== null && servico.custoPecas !== undefined && (
          <p><strong>Custo das Peças:</strong> R$ {Number(servico.custoPecas).toFixed(2)}</p>
        )}
        {servico.custoMaoDeObra !== null && servico.custoMaoDeObra !== undefined && (
          <p><strong>Custo da Mão de Obra:</strong> R$ {Number(servico.custoMaoDeObra).toFixed(2)}</p>
        )}
        {servico.kmAtual !== null && servico.kmAtual !== undefined && (
          <p><strong>KM Atual:</strong> {servico.kmAtual}</p>
        )}
        {servico.proximaRevisaoKm !== null && servico.proximaRevisaoKm !== undefined && (
          <p><strong>Próxima Revisão (KM):</strong> {servico.proximaRevisaoKm}</p>
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

      <Botao type="button" variante="neutro" onClick={() => navigate("/servicos")}>
        Voltar
      </Botao>
    </div>
  );
}

export default ServicoDetailPage;

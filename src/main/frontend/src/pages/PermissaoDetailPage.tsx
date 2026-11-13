import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import Botao from "../components/Botao";
import type { Permissao } from "../types/Permissao";

function PermissaoDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [permissao, setPermissao] = useState<Permissao | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    api.get<Permissao>(`/permissoes/${id}`).then((response) => setPermissao(response.data))
      .catch(() => setErro("Erro ao carregar detalhes da permissão."));
  }, [id]);

  if (erro) return <div><p style={{ color: "red" }}>{erro}</p><Botao type="button" variante="neutro" onClick={() => navigate("/permissoes")}>Voltar</Botao></div>;
  if (!permissao) return <p>Carregando...</p>;
  return <div><h1>{permissao.nome}</h1><p><strong>Descrição:</strong> {permissao.descricao}</p><Botao type="button" variante="neutro" onClick={() => navigate("/permissoes")}>Voltar</Botao></div>;
}

export default PermissaoDetailPage;

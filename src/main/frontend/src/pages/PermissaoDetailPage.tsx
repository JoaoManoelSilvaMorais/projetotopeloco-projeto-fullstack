import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
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

  if (erro) return <div><p style={{ color: "red" }}>{erro}</p><button onClick={() => navigate("/permissoes")}>Voltar</button></div>;
  if (!permissao) return <p>Carregando...</p>;
  return <div><h1>{permissao.nome}</h1><p><strong>Descrição:</strong> {permissao.descricao}</p><button onClick={() => navigate("/permissoes")}>Voltar</button></div>;
}

export default PermissaoDetailPage;

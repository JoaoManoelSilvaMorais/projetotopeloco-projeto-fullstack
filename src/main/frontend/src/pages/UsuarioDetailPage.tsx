import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import Botao from "../components/Botao";
import type { Usuario } from "../types/Usuario";

function UsuarioDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    api
      .get<Usuario>(`/usuarios/${id}`)
      .then((response) => setUsuario(response.data))
      .catch(() => setErro("Erro ao carregar detalhes do usuário."));
  }, [id]);

  if (erro) {
    return (
      <div className="p-4 space-y-3">
        <p className="text-red-600">{erro}</p>
        <Botao type="button" variante="neutro" onClick={() => navigate("/usuarios")}>
          Voltar
        </Botao>
      </div>
    );
  }

  if (!usuario) return <p className="p-4 text-gray-600">Carregando...</p>;

  return (
    <div className="p-6 max-w-xl bg-white rounded-xl shadow-sm border border-gray-200 space-y-4">
      <h1 className="text-2xl font-bold text-gray-900">{usuario.nome}</h1>
      <div className="space-y-2 text-gray-700 text-sm">
        <p><strong>Username:</strong> {usuario.username}</p>
        <p><strong>E-mail:</strong> {usuario.email}</p>
        <p>
          <strong>Status da senha:</strong>{" "}
          {usuario.precisaTrocarSenha
            ? "Senha padrão (troca pendente no próximo login)"
            : "Definida pelo usuário"}
        </p>
      </div>
      <Botao type="button" variante="neutro" onClick={() => navigate("/usuarios")}>
        Voltar
      </Botao>
    </div>
  );
}

export default UsuarioDetailPage;

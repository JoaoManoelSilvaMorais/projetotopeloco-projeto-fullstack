import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import type { Usuario } from "../types/Usuario";

function UsuarioDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState<Usuario | null>(null);
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    api.get<Usuario>(`/usuarios/${id}`).then((response) => setUsuario(response.data))
      .catch(() => setErro("Erro ao carregar detalhes do usuário."));
  }, [id]);

  if (erro) return <div><p style={{ color: "red" }}>{erro}</p><button onClick={() => navigate("/usuarios")}>Voltar</button></div>;
  if (!usuario) return <p>Carregando...</p>;
  return <div><h1>{usuario.nome}</h1><p><strong>Username:</strong> {usuario.username}</p><p><strong>E-mail:</strong> {usuario.email}</p><button onClick={() => navigate("/usuarios")}>Voltar</button></div>;
}

export default UsuarioDetailPage;

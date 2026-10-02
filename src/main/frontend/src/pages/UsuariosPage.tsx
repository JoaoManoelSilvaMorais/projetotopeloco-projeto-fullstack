import { useEffect, useState } from "react";
import api from "../services/api";
import UsuarioForm from "../components/UsuarioForm";
import UsuarioItem from "../components/UsuarioItem";
import type { Usuario } from "../types/Usuario";

function UsuariosPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [usuarioEditando, setUsuarioEditando] = useState<Usuario | null>(null);

  async function carregarUsuarios() {
    const resposta = await api.get<Usuario[]>("/usuarios");
    setUsuarios(resposta.data);
  }

  useEffect(() => {
    void carregarUsuarios();
  }, []);

  async function salvarUsuario(dados: Omit<Usuario, "id">) {
    if (usuarioEditando) {
      await api.put(`/usuarios/${usuarioEditando.id}`, dados);
    } else {
      await api.post("/usuarios", dados);
    }

    setUsuarioEditando(null);
    await carregarUsuarios();
  }

  async function excluirUsuario(id: number) {
    await api.delete(`/usuarios/${id}`);
    await carregarUsuarios();
  }

  return (
    <main>
      <h1>Usuários</h1>
      <UsuarioForm
        key={usuarioEditando?.id ?? "novo"}
        usuarioEditando={usuarioEditando}
        onUsuarioSalvo={salvarUsuario}
      />
      <ul>
        {usuarios.map((usuario) => (
          <li key={usuario.id}>
            <UsuarioItem usuario={usuario} />
            <button type="button" onClick={() => setUsuarioEditando(usuario)}>
              Editar
            </button>
            <button type="button" onClick={() => void excluirUsuario(usuario.id)}>
              Excluir
            </button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default UsuariosPage;

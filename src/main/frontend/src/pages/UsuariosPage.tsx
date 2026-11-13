import { useCallback, useEffect, useState } from "react";
import api from "../services/api";
import UsuarioForm from "../components/UsuarioForm";
import UsuarioItem from "../components/UsuarioItem";
import Botao from "../components/Botao";
import type { Usuario } from "../types/Usuario";

function UsuariosPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [usuarioEditando, setUsuarioEditando] = useState<Usuario | null>(null);
  const [senhaPadrao, setSenhaPadrao] = useState("123456");
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [mensagemSucesso, setMensagemSucesso] = useState<string | null>(null);

  const carregarUsuarios = useCallback(async () => {
    setCarregando(true);
    setErro(null);
    try {
      const resposta = await api.get<Usuario[]>("/usuarios");
      setUsuarios(resposta.data);
    } catch {
      setErro("Não foi possível carregar os usuários.");
    } finally {
      setCarregando(false);
    }
  }, []);

  useEffect(() => {
    let ativo = true;

    async function inicializar() {
      try {
        const [usuariosResp, senhaResp] = await Promise.all([
          api.get<Usuario[]>("/usuarios"),
          api.get<{ senhaPadrao: string }>("/usuarios/senha-padrao").catch(() => null),
        ]);
        if (!ativo) return;
        setUsuarios(usuariosResp.data);
        if (senhaResp?.data?.senhaPadrao) {
          setSenhaPadrao(senhaResp.data.senhaPadrao);
        }
      } catch {
        if (ativo) {
          setErro("Não foi possível carregar os usuários.");
        }
      } finally {
        if (ativo) {
          setCarregando(false);
        }
      }
    }

    void inicializar();
    return () => {
      ativo = false;
    };
  }, []);

  async function salvarUsuario(dados: Omit<Usuario, "id">) {
    setMensagemSucesso(null);

    if (usuarioEditando) {
      await api.put(`/usuarios/${usuarioEditando.id}`, dados);
      setMensagemSucesso(`Usuário "${dados.username}" atualizado com sucesso.`);
    } else {
      const resposta = await api.post<Usuario>("/usuarios", dados);
      setMensagemSucesso(
        `Usuário "${resposta.data.username}" cadastrado com sucesso! Senha padrão definida: ${senhaPadrao}. Ao fazer login, o usuário será alertado para trocar a senha imediatamente.`
      );
    }

    setUsuarioEditando(null);
    await carregarUsuarios();
  }

  async function excluirUsuario(id: number) {
    setMensagemSucesso(null);
    await api.delete(`/usuarios/${id}`);
    await carregarUsuarios();
  }

  return (
    <main className="space-y-4">
      <h1 className="text-2xl font-bold text-gray-900">Usuários</h1>

      <UsuarioForm
        key={usuarioEditando?.id ?? "novo"}
        usuarioEditando={usuarioEditando}
        senhaPadrao={senhaPadrao}
        onCancelarEdicao={() => setUsuarioEditando(null)}
        onUsuarioSalvo={salvarUsuario}
      />

      {mensagemSucesso && (
        <div
          role="status"
          className="p-4 rounded-lg bg-green-50 border border-green-200 text-green-800 text-sm"
        >
          {mensagemSucesso}
        </div>
      )}

      {carregando && <p className="text-gray-600">Carregando...</p>}
      {erro && (
        <p role="alert" className="text-red-600 text-sm">
          {erro}
        </p>
      )}

      {!carregando && !erro && (
        <ul className="space-y-3">
          {usuarios.map((usuario) => (
            <li
              key={usuario.id}
              className="bg-white p-4 rounded-xl border border-gray-200 shadow-sm flex flex-wrap items-center justify-between gap-3"
            >
              <div className="flex flex-wrap items-center gap-2">
                <UsuarioItem usuario={usuario} />
                {usuario.precisaTrocarSenha && (
                  <span className="inline-block px-2.5 py-0.5 text-xs font-semibold rounded-full bg-amber-100 text-amber-800 border border-amber-200">
                    Senha padrão (troca pendente)
                  </span>
                )}
              </div>
              <div className="flex items-center gap-2">
                <Botao
                  type="button"
                  variante="neutro"
                  onClick={() => setUsuarioEditando(usuario)}
                >
                  Editar
                </Botao>
                <Botao
                  type="button"
                  variante="perigo"
                  onClick={() => void excluirUsuario(usuario.id)}
                >
                  Excluir
                </Botao>
              </div>
            </li>
          ))}
        </ul>
      )}
    </main>
  );
}

export default UsuariosPage;

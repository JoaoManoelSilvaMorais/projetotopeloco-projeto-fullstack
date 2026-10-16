import { useEffect, useState } from "react";
import api from "../services/api";
import type { Usuario } from "../types/Usuario";
import type { Permissao } from "../types/Permissao";

function UsuarioPermissoesPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [permissoes, setPermissoes] = useState<Permissao[]>([]);
  const [usuarioId, setUsuarioId] = useState<number | null>(null);
  const [idsSelecionados, setIdsSelecionados] = useState<number[]>([]);
  const [erro, setErro] = useState<string | null>(null);
  const [mensagem, setMensagem] = useState<string | null>(null);

  useEffect(() => {
    Promise.all([
      api.get<Usuario[]>("/usuarios"),
      api.get<Permissao[]>("/permissoes"),
    ]).then(([usuariosResponse, permissoesResponse]) => {
      setUsuarios(usuariosResponse.data);
      setPermissoes(permissoesResponse.data);
    }).catch(() => setErro("Não foi possível carregar usuários e permissões."));
  }, []);

  function selecionarUsuario(id: number) {
    setUsuarioId(id || null);
    setIdsSelecionados([]);
    if (id) {
      api.get<Usuario>(`/usuarios/${id}/permissoes`)
        .then((response) => setIdsSelecionados(response.data.permissoes?.map((p) => p.id) ?? []))
        .catch(() => setErro("Não foi possível carregar as permissões do usuário."));
    }
  }

  function alternarPermissao(id: number) {
    setIdsSelecionados((prev) => prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]);
  }

  async function salvar() {
    if (usuarioId === null) return;
    setErro(null);
    setMensagem(null);
    try {
      await api.put(`/usuarios/${usuarioId}/permissoes`, idsSelecionados);
      setMensagem("Permissões salvas com sucesso.");
    } catch {
      setErro("Não foi possível salvar as permissões.");
    }
  }

  return (
    <div>
      <h1>Atribuir Permissões</h1>
      {erro && <p style={{ color: "red" }}>{erro}</p>}
      {mensagem && <p style={{ color: "green" }}>{mensagem}</p>}
      <label>Usuário: <select value={usuarioId ?? ""} onChange={(event) => selecionarUsuario(Number(event.target.value))}>
        <option value="">Selecione...</option>
        {usuarios.map((usuario) => <option key={usuario.id} value={usuario.id}>{usuario.nome} ({usuario.username})</option>)}
      </select></label>
      <fieldset>
        <legend>Permissões</legend>
        {permissoes.map((permissao) => <label key={permissao.id} style={{ display: "block" }}>
          <input type="checkbox" checked={idsSelecionados.includes(permissao.id)} onChange={() => alternarPermissao(permissao.id)} />
          {" "}{permissao.nome} — {permissao.descricao}
        </label>)}
      </fieldset>
      <button type="button" onClick={() => void salvar()} disabled={usuarioId === null}>Salvar permissões</button>
    </div>
  );
}

export default UsuarioPermissoesPage;

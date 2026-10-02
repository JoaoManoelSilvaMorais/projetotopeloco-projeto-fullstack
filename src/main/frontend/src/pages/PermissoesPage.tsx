import { useEffect, useState } from "react";
import api from "../services/api";
import PermissaoForm from "../components/PermissaoForm";
import PermissaoItem from "../components/PermissaoItem";
import type { Permissao } from "../types/Permissao";

function PermissoesPage() {
  const [permissoes, setPermissoes] = useState<Permissao[]>([]);
  const [permissaoEditando, setPermissaoEditando] = useState<Permissao | null>(null);

  async function carregarPermissoes() {
    const resposta = await api.get<Permissao[]>("/permissao");
    setPermissoes(resposta.data);
  }

  useEffect(() => {
    void carregarPermissoes();
  }, []);

  async function salvarPermissao(dados: Omit<Permissao, "id">) {
    if (permissaoEditando) {
      await api.put(`/permissao/${permissaoEditando.id}`, dados);
    } else {
      await api.post("/permissao", dados);
    }

    setPermissaoEditando(null);
    await carregarPermissoes();
  }

  async function excluirPermissao(id: number) {
    await api.delete(`/permissao/${id}`);
    await carregarPermissoes();
  }

  return (
    <main>
      <h1>Permissões</h1>
      <PermissaoForm
        key={permissaoEditando?.id ?? "nova"}
        permissaoEditando={permissaoEditando}
        onPermissaoSalva={salvarPermissao}
      />
      <ul>
        {permissoes.map((permissao) => (
          <li key={permissao.id}>
            <PermissaoItem permissao={permissao} />
            <button type="button" onClick={() => setPermissaoEditando(permissao)}>
              Editar
            </button>
            <button type="button" onClick={() => void excluirPermissao(permissao.id)}>
              Excluir
            </button>
          </li>
        ))}
      </ul>
    </main>
  );
}

export default PermissoesPage;

import { useEffect, useState } from "react";
import api from "../services/api";
import PermissaoForm from "../components/PermissaoForm";
import PermissaoItem from "../components/PermissaoItem";
import type { Permissao } from "../types/Permissao";

function PermissoesPage() {
  const [permissoes, setPermissoes] = useState<Permissao[]>([]);
  const [permissaoEditando, setPermissaoEditando] = useState<Permissao | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  async function carregarPermissoes() {
    setCarregando(true);
    setErro(null);
    try {
      const resposta = await api.get<Permissao[]>("/permissoes");
      setPermissoes(resposta.data);
    } catch {
      setErro("Não foi possível carregar as permissões.");
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    void carregarPermissoes();
  }, []);

  async function salvarPermissao(dados: Omit<Permissao, "id">) {
    if (permissaoEditando) {
      await api.put(`/permissoes/${permissaoEditando.id}`, dados);
    } else {
      await api.post("/permissoes", dados);
    }

    setPermissaoEditando(null);
    await carregarPermissoes();
  }

  async function excluirPermissao(id: number) {
    await api.delete(`/permissoes/${id}`);
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
      {carregando && <p>Carregando...</p>}
      {erro && <p style={{ color: "red" }}>{erro}</p>}
      {!carregando && !erro && <ul>
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
      </ul>}
    </main>
  );
}

export default PermissoesPage;

import { useState } from "react";
import type { FormEvent } from "react";
import api from "../services/api";
import type { Permissao } from "../types/Permissao";

interface PermissaoFormProps {
  onPermissaoSalva: () => void | Promise<void>;
  permissaoEditando?: Permissao | null;
}

function PermissaoForm({ onPermissaoSalva, permissaoEditando }: PermissaoFormProps) {
  const [nome, setNome] = useState(permissaoEditando?.nome ?? "");
  const [descricao, setDescricao] = useState(permissaoEditando?.descricao ?? "");
  const [erro, setErro] = useState<string | null>(null);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro(null);
    const dados = { nome, descricao };

    try {
      if (permissaoEditando) {
        await api.put(`/permissoes/${permissaoEditando.id}`, dados);
      } else {
        await api.post("/permissoes", dados);
        setNome("");
        setDescricao("");
      }
      await onPermissaoSalva();
    } catch {
      setErro("Erro ao salvar permissão. Verifique se o back-end está rodando.");
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        value={nome}
        onChange={(event) => setNome(event.target.value)}
        placeholder="Nome da permissão"
        required
      />
      <input
        value={descricao}
        onChange={(event) => setDescricao(event.target.value)}
        placeholder="Descrição"
        required
      />
      <button type="submit">
        {permissaoEditando ? "Salvar alterações" : "Cadastrar"}
      </button>
      {erro && <p style={{ color: "red" }}>{erro}</p>}
    </form>
  );
}

export default PermissaoForm;

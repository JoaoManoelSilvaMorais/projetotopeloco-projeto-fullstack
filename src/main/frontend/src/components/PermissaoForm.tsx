import { useState } from "react";
import type { FormEvent } from "react";
import type { Permissao } from "../types/Permissao";

interface PermissaoFormProps {
  onPermissaoSalva: (dados: Omit<Permissao, "id">) => void | Promise<void>;
  permissaoEditando?: Permissao | null;
}

function PermissaoForm({ onPermissaoSalva, permissaoEditando }: PermissaoFormProps) {
  const [nome, setNome] = useState(permissaoEditando?.nome ?? "");
  const [descricao, setDescricao] = useState(permissaoEditando?.descricao ?? "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const dados = { nome, descricao };

    await onPermissaoSalva(dados);
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
    </form>
  );
}

export default PermissaoForm;

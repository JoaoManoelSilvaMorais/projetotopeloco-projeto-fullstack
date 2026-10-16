// src/components/UsuarioForm.tsx

//Ta dando um erro de depreciação, mas não é nada demais,
//é só uma questão de atualização do React. O código ainda 
// funciona normalmente.

import { useState } from "react";
import type { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import type { Usuario } from "../types/Usuario";

interface UsuarioFormProps {
  onUsuarioSalvo: (dados: Omit<Usuario, "id">) => void | Promise<void>;
  usuarioEditando?: Usuario | null;
}

function UsuarioForm({ onUsuarioSalvo, usuarioEditando }: UsuarioFormProps) {
  const [nome, setNome] = useState(usuarioEditando?.nome ?? "");
  const [username, setUsername] = useState(usuarioEditando?.username ?? "");
  const [email, setEmail] = useState(usuarioEditando?.email ?? "");
  const navigate = useNavigate();

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const dados = { nome, username, email };

    await onUsuarioSalvo(dados);
    navigate("/usuarios");
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        value={nome}
        onChange={(e) => setNome(e.target.value)}
        placeholder="Nome"
      />
      <input
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        placeholder="Username"
      />
      <input
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="E-mail"
      />
      <button type="submit">
        {usuarioEditando ? "Salvar alterações" : "Cadastrar"}
      </button>
    </form>
  );
}

export default UsuarioForm;
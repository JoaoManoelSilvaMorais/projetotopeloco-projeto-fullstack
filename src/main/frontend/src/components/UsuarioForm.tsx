// src/components/UsuarioForm.tsx

//Ta dando um erro de depreciação, mas não é nada demais,
//é só uma questão de atualização do React. O código ainda 
// funciona normalmente.

import { useState } from "react";
import type { FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import type { Usuario } from "../types/Usuario";
import Botao from "./Botao";

interface UsuarioFormProps {
  onUsuarioSalvo: (dados: Omit<Usuario, "id">) => void | Promise<void>;
  usuarioEditando?: Usuario | null;
  senhaPadrao?: string;
  onCancelarEdicao?: () => void;
}

function UsuarioForm({
  onUsuarioSalvo,
  usuarioEditando,
  senhaPadrao = "123456",
  onCancelarEdicao,
}: UsuarioFormProps) {
  const [nome, setNome] = useState(usuarioEditando?.nome ?? "");
  const [username, setUsername] = useState(usuarioEditando?.username ?? "");
  const [email, setEmail] = useState(usuarioEditando?.email ?? "");
  const [erro, setErro] = useState<string | null>(null);
  const [salvando, setSalvando] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro(null);

    const dados = {
      nome: nome.trim(),
      username: username.trim(),
      email: email.trim(),
    };

    if (!dados.nome || !dados.username || !dados.email) {
      setErro("Preencha nome, username e e-mail.");
      return;
    }

    setSalvando(true);
    try {
      await onUsuarioSalvo(dados);
      if (!usuarioEditando) {
        setNome("");
        setUsername("");
        setEmail("");
      }
      navigate("/usuarios");
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setErro(
          err.response?.data?.mensagem ||
            err.response?.data?.message ||
            "Não foi possível salvar o usuário."
        );
      } else {
        setErro("Não foi possível salvar o usuário.");
      }
    } finally {
      setSalvando(false);
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white p-5 rounded-xl border border-gray-200 shadow-sm space-y-4 mb-6"
    >
      {!usuarioEditando && (
        <div className="p-3 rounded-lg bg-blue-50 border border-blue-200 text-blue-900 text-sm">
          <span className="font-semibold">Senha padrão inicial:</span>{" "}
          <code className="px-1.5 py-0.5 rounded bg-blue-100 font-mono font-semibold">
            {senhaPadrao}
          </code>{" "}
          — Ao cadastrar o usuário, esta senha será definida automaticamente e ele será alertado
          a alterá-la imediatamente após o login.
        </div>
      )}

      {erro && (
        <div
          role="alert"
          className="p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm"
        >
          {erro}
        </div>
      )}

      <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
        <input
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          placeholder="Nome"
          required
          className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <input
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          placeholder="Username"
          required
          className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="E-mail"
          required
          className="px-3 py-2 border border-gray-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div className="flex items-center gap-2">
        <Botao type="submit" variante="primario" disabled={salvando}>
          {salvando
            ? "Salvando..."
            : usuarioEditando
              ? "Salvar alterações"
              : "Cadastrar"}
        </Botao>
        {usuarioEditando && onCancelarEdicao && (
          <Botao type="button" variante="neutro" onClick={onCancelarEdicao}>
            Cancelar
          </Botao>
        )}
      </div>
    </form>
  );
}

export default UsuarioForm;

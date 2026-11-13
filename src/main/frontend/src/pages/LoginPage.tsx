import { useState, useEffect } from "react";
import type { FormEvent } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import api from "../services/api";
import { salvarSessao, estaAutenticado, precisaTrocarSenha } from "../services/auth";
import type { DadosSessao } from "../services/auth";
import Botao from "../components/Botao";

export function LoginPage() {
  const [username, setUsername] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (estaAutenticado()) {
      navigate(precisaTrocarSenha() ? "/alterar-senha" : "/", { replace: true });
    }
  }, [navigate]);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro(null);

    const trimmedUsername = username.trim();
    if (!trimmedUsername || !senha.trim()) {
      setErro("Por favor, preencha todos os campos.");
      return;
    }

    setCarregando(true);
    try {
      const response = await api.post<DadosSessao>("/login", {
        username: trimmedUsername,
        senha,
      });

      salvarSessao(response.data);

      if (response.data.precisaTrocarSenha) {
        navigate("/alterar-senha", {
          replace: true,
          state: { alertaTrocaSenha: true },
        });
        return;
      }

      const state = location.state as { from?: { pathname: string } } | null;
      const destino = state?.from?.pathname || "/";
      navigate(destino, { replace: true });
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        if (err.response?.status === 401) {
          const mensagemServidor = err.response.data?.mensagem;
          setErro(mensagemServidor || "Credenciais inválidas");
        } else if (err.code === "ERR_NETWORK") {
          setErro("Servidor indisponível. Verifique se o backend está em execução na porta 8080.");
        } else {
          setErro("Falha na autenticação. Verifique suas credenciais e tente novamente.");
        }
      } else {
        setErro("Ocorreu um erro inesperado. Tente novamente.");
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-6 bg-white p-8 rounded-xl shadow-md border border-gray-100">
        <div className="text-center">
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">GarageLog</h1>
          <p className="mt-2 text-sm text-gray-600">Sistema de Gestão de Veículos e Serviços</p>
          <h2 className="mt-4 text-xl font-semibold text-gray-800">Acesse sua conta</h2>
        </div>

        {erro && (
          <div
            role="alert"
            className="p-3 rounded-lg bg-red-50 border border-red-200 text-red-700 text-sm flex items-start gap-2"
          >
            <svg
              className="w-5 h-5 flex-shrink-0 text-red-500 mt-0.5"
              fill="currentColor"
              viewBox="0 0 20 20"
              aria-hidden="true"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z"
                clipRule="evenodd"
              />
            </svg>
            <span>{erro}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label
              htmlFor="username"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              Usuário
            </label>
            <input
              id="username"
              name="username"
              type="text"
              autoComplete="username"
              required
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Ex: admin ou user"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
            />
          </div>

          <div>
            <label
              htmlFor="senha"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              Senha
            </label>
            <input
              id="senha"
              name="senha"
              type="password"
              autoComplete="current-password"
              required
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              placeholder="••••••••"
              className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-sm"
            />
          </div>

          <div className="pt-2">
            <Botao
              type="submit"
              variante="primario"
              disabled={carregando}
              className="w-full flex justify-center py-2.5 text-base"
            >
              {carregando ? "Entrando..." : "Entrar"}
            </Botao>
          </div>
        </form>

        <div className="mt-6 pt-4 border-t border-gray-100 text-xs text-gray-500 bg-gray-50 p-3 rounded-lg">
          <p className="font-semibold text-gray-700 mb-1">Acesso para demonstração:</p>
          <ul className="space-y-0.5">
            <li><span className="font-medium text-gray-700">Administrador:</span> admin / admin123</li>
            <li><span className="font-medium text-gray-700">Usuário padrão:</span> user / user123</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;

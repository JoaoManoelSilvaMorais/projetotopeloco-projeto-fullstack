import { useState, type FormEvent } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import api from "../services/api";
import Botao from "../components/Botao";
import { marcarSenhaAlterada, obterSessao, precisaTrocarSenha } from "../services/auth";

export function AlterarSenhaPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const sessao = obterSessao();

  const [senhaAtual, setSenhaAtual] = useState("");
  const [novaSenha, setNovaSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [sucesso, setSucesso] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);

  const locationState = location.state as { alertaTrocaSenha?: boolean } | null;
  const alertaPendente = !sucesso && (precisaTrocarSenha() || Boolean(locationState?.alertaTrocaSenha));

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setErro(null);
    setSucesso(null);

    if (!senhaAtual.trim() || !novaSenha.trim() || !confirmarSenha.trim()) {
      setErro("Preencha todos os campos.");
      return;
    }

    if (novaSenha.trim().length < 6) {
      setErro("A nova senha deve ter pelo menos 6 caracteres.");
      return;
    }

    if (novaSenha !== confirmarSenha) {
      setErro("A confirmação da nova senha não confere.");
      return;
    }

    if (novaSenha === senhaAtual) {
      setErro("A nova senha deve ser diferente da senha atual.");
      return;
    }

    setCarregando(true);
    try {
      await api.post("/alterar-senha", { senhaAtual, novaSenha });
      marcarSenhaAlterada();
      setSenhaAtual("");
      setNovaSenha("");
      setConfirmarSenha("");
      setSucesso("Senha alterada com sucesso! Sua conta está protegida com a nova senha.");
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setErro(err.response?.data?.mensagem || "Não foi possível alterar a senha.");
      } else {
        setErro("Ocorreu um erro inesperado ao alterar a senha.");
      }
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="max-w-lg mx-auto bg-white p-8 rounded-xl shadow-md border border-gray-100 space-y-5">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Alterar Senha</h1>
        {sessao && (
          <p className="text-sm text-gray-600 mt-1">
            Usuário: <span className="font-medium text-gray-800">{sessao.username}</span>
          </p>
        )}
      </div>

      {alertaPendente && (
        <div
          role="alert"
          className="p-4 rounded-lg bg-amber-50 border border-amber-300 text-amber-900 text-sm space-y-1"
        >
          <p className="font-bold text-amber-950">
            Atenção: Troca de senha imediata recomendada
          </p>
          <p>
            Você acessou utilizando a senha padrão inicial definida no cadastro. Por segurança,
            altere sua senha imediatamente antes de continuar utilizando o sistema.
          </p>
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

      {sucesso && (
        <div
          role="status"
          className="p-4 rounded-lg bg-green-50 border border-green-200 text-green-800 text-sm space-y-3"
        >
          <p className="font-medium">{sucesso}</p>
          <div>
            <Botao type="button" variante="primario" onClick={() => navigate("/", { replace: true })}>
              Ir para a página inicial
            </Botao>
          </div>
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="senhaAtual" className="block text-sm font-medium text-gray-700 mb-1">
            Senha atual
          </label>
          <input
            id="senhaAtual"
            type="password"
            autoComplete="current-password"
            required
            value={senhaAtual}
            onChange={(e) => setSenhaAtual(e.target.value)}
            placeholder="Digite sua senha atual ou padrão"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
          />
        </div>

        <div>
          <label htmlFor="novaSenha" className="block text-sm font-medium text-gray-700 mb-1">
            Nova senha
          </label>
          <input
            id="novaSenha"
            type="password"
            autoComplete="new-password"
            required
            value={novaSenha}
            onChange={(e) => setNovaSenha(e.target.value)}
            placeholder="Mínimo de 6 caracteres"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
          />
        </div>

        <div>
          <label htmlFor="confirmarSenha" className="block text-sm font-medium text-gray-700 mb-1">
            Confirmar nova senha
          </label>
          <input
            id="confirmarSenha"
            type="password"
            autoComplete="new-password"
            required
            value={confirmarSenha}
            onChange={(e) => setConfirmarSenha(e.target.value)}
            placeholder="Repita a nova senha"
            className="w-full px-3 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
          />
        </div>

        <div className="flex items-center gap-3 pt-2">
          <Botao type="submit" variante="primario" disabled={carregando}>
            {carregando ? "Salvando..." : "Salvar nova senha"}
          </Botao>
          <Botao type="button" variante="neutro" onClick={() => navigate(-1)}>
            Voltar
          </Botao>
        </div>
      </form>
    </div>
  );
}

export default AlterarSenhaPage;

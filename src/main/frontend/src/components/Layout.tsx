import React from "react";
import { NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import Botao from "./Botao";
import { obterSessao, obterToken, precisaTrocarSenha, sair, temPapel } from "../services/auth";

export const Layout: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const isAdmin = temPapel("ADMIN");
  const autenticado = Boolean(obterToken());
  const sessao = obterSessao();
  const deveTrocarSenha = precisaTrocarSenha();

  function handleSair() {
    sair();
    navigate("/login");
  }

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `px-3 py-2 rounded-md text-sm font-medium transition-colors ${
      isActive
        ? "bg-blue-600 text-white"
        : "text-gray-700 hover:bg-gray-100 hover:text-gray-900"
    }`;

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      <header className="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center space-x-4">
              <span className="text-xl font-bold text-gray-900 tracking-tight">
                GarageLog
              </span>
              <nav className="flex items-center space-x-2">
                <NavLink to="/" className={linkClass}>
                  Home
                </NavLink>

                {/* Links administrativos visíveis exclusivamente para perfil ADMIN */}
                {isAdmin && (
                  <>
                    <NavLink to="/usuarios" className={linkClass}>
                      Usuários
                    </NavLink>
                    <NavLink to="/permissoes" className={linkClass}>
                      Permissões
                    </NavLink>
                    <NavLink to="/atribuir-permissoes" className={linkClass}>
                      Atribuir Permissões
                    </NavLink>
                  </>
                )}

                <NavLink to="/veiculos" className={linkClass}>
                  Veículos
                </NavLink>
                <NavLink to="/servicos-manutencao" className={linkClass}>
                  Serviços de Manutenção
                </NavLink>
                <NavLink to="/despesas-recorrentes" className={linkClass}>
                  Despesas Recorrentes
                </NavLink>
                <NavLink to="/alterar-senha" className={linkClass}>
                  Alterar Senha
                </NavLink>
              </nav>
            </div>

            {/* Controle de sessão e botão Sair */}
            {autenticado && (
              <div className="flex items-center space-x-3">
                {sessao && (
                  <span className="text-sm text-gray-600 hidden md:inline">
                    {sessao.nome || sessao.username}
                    {isAdmin && (
                      <span className="ml-2 px-2 py-0.5 text-xs font-semibold rounded bg-blue-100 text-blue-800">
                        ADMIN
                      </span>
                    )}
                  </span>
                )}
                <Botao type="button" variante="neutro" onClick={handleSair}>
                  Sair
                </Botao>
              </div>
            )}
          </div>
        </div>
      </header>

      {deveTrocarSenha && location.pathname !== "/alterar-senha" && (
        <div
          role="alert"
          className="bg-amber-50 border-b border-amber-300 text-amber-900"
        >
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-3 flex flex-wrap items-center justify-between gap-3">
            <p className="text-sm font-medium">
              <strong>Alerta de Segurança:</strong> Você está utilizando a senha padrão inicial.
              Altere sua senha imediatamente.
            </p>
            <Botao
              type="button"
              variante="perigo"
              onClick={() => navigate("/alterar-senha")}
            >
              Alterar senha agora
            </Botao>
          </div>
        </div>
      )}

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 flex-1 w-full">
        <Outlet />
      </main>
    </div>
  );
};

export default Layout;

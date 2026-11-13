import React from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { obterToken, temPapel } from "../services/auth";

export interface RequireAuthProps {
  children?: React.ReactNode;
  papelNecessario?: string;
}

/**
 * Componente de proteção de rotas no React Router DOM v6.
 * - Verifica a presença de token JWT ativo via `obterToken()`.
 * - Se não autenticado, redireciona para `/login`, gravando a rota requisitada no `state.from`.
 * - Se `papelNecessario` for especificado, verifica se o usuário possui a permissão via `temPapel()`.
 *   Se não possuir, redireciona para `/` (Home).
 * - Se autenticado e autorizado, renderiza `children` ou `<Outlet />` para rotas aninhadas.
 */
export const RequireAuth: React.FC<RequireAuthProps> = ({
  children,
  papelNecessario,
}) => {
  const token = obterToken();
  const location = useLocation();

  if (!token) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (papelNecessario && !temPapel(papelNecessario)) {
    return <Navigate to="/" replace />;
  }

  return children ? <>{children}</> : <Outlet />;
};

export default RequireAuth;

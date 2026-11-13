import { Link } from "react-router-dom";
import { temPapel } from "../services/auth";

export function Home() {
  const isAdmin = temPapel("ADMIN");

  return (
    <div className="space-y-4">
      <h1 className="text-2xl font-bold text-gray-900">Bem-vindo ao sistema GarageLog!</h1>
      <p className="text-gray-700">Sistema de gerenciamento Full Stack (Spring Boot + React).</p>
      <div className="flex flex-wrap items-center gap-2 pt-2">
        {isAdmin && (
          <>
            <Link to="/usuarios" className="text-blue-600 hover:underline">
              Usuários
            </Link>
            <span className="text-gray-400">|</span>
            <Link to="/permissoes" className="text-blue-600 hover:underline">
              Permissões
            </Link>
            <span className="text-gray-400">|</span>
            <Link to="/atribuir-permissoes" className="text-blue-600 hover:underline">
              Atribuir Permissões
            </Link>
            <span className="text-gray-400">|</span>
          </>
        )}
        <Link to="/veiculos" className="text-blue-600 hover:underline">
          Veículos
        </Link>
        <span className="text-gray-400">|</span>
        <Link to="/servicos-manutencao" className="text-blue-600 hover:underline">
          Serviços de Manutenção
        </Link>
        <span className="text-gray-400">|</span>
        <Link to="/despesas-recorrentes" className="text-blue-600 hover:underline">
          Despesas Recorrentes
        </Link>
      </div>
    </div>
  );
}

export default Home;

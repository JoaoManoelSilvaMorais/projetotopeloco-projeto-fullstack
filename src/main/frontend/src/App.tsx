import { Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import RequireAuth from "./components/RequireAuth";
import LoginPage from "./pages/LoginPage";
import Home from "./pages/Home";
import UsuariosPage from "./pages/UsuariosPage";
import UsuarioDetailPage from "./pages/UsuarioDetailPage";
import PermissoesPage from "./pages/PermissoesPage";
import PermissaoDetailPage from "./pages/PermissaoDetailPage";
import UsuarioPermissoesPage from "./pages/UsuarioPermissoesPage";
import VeiculosPage from "./pages/VeiculosPage";
import VeiculoDetailPage from "./pages/VeiculoDetailPage";
import ServicosManutencaoPage from "./pages/ServicosManutencaoPage";
import ServicoDetailPage from "./pages/ServicoDetailPage";
import DespesasRecorrentesPage from "./pages/DespesasRecorrentesPage";
import DespesaDetailPage from "./pages/DespesaDetailPage";
import AlterarSenhaPage from "./pages/AlterarSenhaPage";

function App() {
  return (
    <Routes>
      {/* Rota pública de login */}
      <Route path="/login" element={<LoginPage />} />

      {/* Rotas protegidas por autenticação */}
      <Route element={<RequireAuth />}>
        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path="/alterar-senha" element={<AlterarSenhaPage />} />

          {/* Veículos */}
          <Route path="/veiculos" element={<VeiculosPage />} />
          <Route path="/veiculos/:id" element={<VeiculoDetailPage />} />

          {/* Serviços de Manutenção */}
          <Route path="/servicos-manutencao" element={<ServicosManutencaoPage />} />
          <Route path="/servicos" element={<ServicosManutencaoPage />} />
          <Route path="/servicos/:id" element={<ServicoDetailPage />} />
          <Route path="/servicos-manutencao/:id" element={<ServicoDetailPage />} />

          {/* Despesas Recorrentes */}
          <Route path="/despesas-recorrentes" element={<DespesasRecorrentesPage />} />
          <Route path="/despesas" element={<DespesasRecorrentesPage />} />
          <Route path="/despesas/:id" element={<DespesaDetailPage />} />
          <Route path="/despesas-recorrentes/:id" element={<DespesaDetailPage />} />

          {/* Rotas administrativas restritas a ADMIN */}
          <Route element={<RequireAuth papelNecessario="ADMIN" />}>
            <Route path="/usuarios" element={<UsuariosPage />} />
            <Route path="/usuarios/:id" element={<UsuarioDetailPage />} />
            <Route path="/permissoes" element={<PermissoesPage />} />
            <Route path="/permissoes/:id" element={<PermissaoDetailPage />} />
            <Route path="/atribuir-permissoes" element={<UsuarioPermissoesPage />} />
          </Route>

          {/* Rota 404 para caminhos não encontrados */}
          <Route
            path="*"
            element={
              <div className="text-center py-12">
                <h1 className="text-2xl font-bold text-gray-800">Página não encontrada</h1>
                <p className="text-gray-600 mt-2">O endereço solicitado não foi localizado.</p>
              </div>
            }
          />
        </Route>
      </Route>
    </Routes>
  );
}

export default App;

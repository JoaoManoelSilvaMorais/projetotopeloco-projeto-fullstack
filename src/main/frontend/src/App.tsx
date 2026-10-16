import { Route, Routes } from "react-router-dom";
import "./App.css";
import Layout from "./components/Layout";
import DespesasRecorrentesPage from "./pages/DespesasRecorrentesPage";
import Home from "./pages/Home";
import PermissaoDetailPage from "./pages/PermissaoDetailPage";
import PermissoesPage from "./pages/PermissoesPage";
import ServicosManutencaoPage from "./pages/ServicosManutencaoPage";
import UsuarioDetailPage from "./pages/UsuarioDetailPage";
import UsuarioPermissoesPage from "./pages/UsuarioPermissoesPage";
import UsuariosPage from "./pages/UsuariosPage";
import VeiculoDetailPage from "./pages/VeiculoDetailPage";
import VeiculosPage from "./pages/VeiculosPage";

function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route path="/" element={<Home />} />
        <Route path="/usuarios" element={<UsuariosPage />} />
        <Route path="/usuarios/:id" element={<UsuarioDetailPage />} />
        <Route path="/permissoes" element={<PermissoesPage />} />
        <Route path="/permissoes/:id" element={<PermissaoDetailPage />} />
        <Route path="/atribuir-permissoes" element={<UsuarioPermissoesPage />} />
        <Route path="/veiculos" element={<VeiculosPage />} />
        <Route path="/veiculos/:id" element={<VeiculoDetailPage />} />
        <Route path="/servicos-manutencao" element={<ServicosManutencaoPage />} />
        <Route path="/despesas-recorrentes" element={<DespesasRecorrentesPage />} />
        <Route path="*" element={<h1>Página não encontrada</h1>} />
      </Route>
    </Routes>
  );
}

export default App;

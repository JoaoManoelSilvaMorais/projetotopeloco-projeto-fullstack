import { useState } from "react";
import DespesasRecorrentesPage from "./pages/DespesasRecorrentesPage";
import PermissoesPage from "./pages/PermissoesPage";
import ServicosManutencaoPage from "./pages/ServicosManutencaoPage";
import UsuariosPage from "./pages/UsuariosPage";
import VeiculosPage from "./pages/VeiculosPage";

type Pagina = "usuarios" | "permissoes" | "veiculos" | "servicos" | "despesas";

function App() {
  const [pagina, setPagina] = useState<Pagina>("usuarios");

  return (
    <>
      <nav aria-label="Navegação principal">
        <button type="button" onClick={() => setPagina("usuarios")}>Usuários</button>
        <button type="button" onClick={() => setPagina("permissoes")}>Permissões</button>
        <button type="button" onClick={() => setPagina("veiculos")}>Veículos</button>
        <button type="button" onClick={() => setPagina("servicos")}>Manutenção</button>
        <button type="button" onClick={() => setPagina("despesas")}>Despesas</button>
      </nav>
      {pagina === "usuarios" && <UsuariosPage />}
      {pagina === "permissoes" && <PermissoesPage />}
      {pagina === "veiculos" && <VeiculosPage />}
      {pagina === "servicos" && <ServicosManutencaoPage />}
      {pagina === "despesas" && <DespesasRecorrentesPage />}
    </>
  );
}
export default App;

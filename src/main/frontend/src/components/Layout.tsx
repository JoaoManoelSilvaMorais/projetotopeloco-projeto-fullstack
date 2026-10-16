import { NavLink, Outlet } from "react-router-dom";

function Layout() {
  const linkStyle = ({ isActive }: { isActive: boolean }) => ({
    marginRight: "12px",
    fontWeight: isActive ? "bold" : "normal",
    textDecoration: isActive ? "underline" : "none",
  });

  return (
    <div>
      <nav style={{ padding: "12px 0", borderBottom: "1px solid #ccc", marginBottom: "20px" }}>
        <NavLink style={linkStyle} to="/">Home</NavLink>
        <NavLink style={linkStyle} to="/usuarios">Usuários</NavLink>
        <NavLink style={linkStyle} to="/permissoes">Permissões</NavLink>
        <NavLink style={linkStyle} to="/atribuir-permissoes">Atribuir Permissões</NavLink>
        <NavLink style={linkStyle} to="/veiculos">Veículos</NavLink>
        <NavLink style={linkStyle} to="/servicos-manutencao">Serviços de Manutenção</NavLink>
        <NavLink style={linkStyle} to="/despesas-recorrentes">Despesas Recorrentes</NavLink>
      </nav>
      <main><Outlet /></main>
    </div>
  );
}

export default Layout;

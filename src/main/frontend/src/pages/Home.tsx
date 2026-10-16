import { Link } from "react-router-dom";

function Home() {
  return (
    <div>
      <h1>Bem-vindo ao sistema GarageLog!</h1>
      <p>Sistema de gerenciamento Full Stack (Spring Boot + React).</p>
      <p>
        <Link to="/usuarios">Usuários</Link>{" | "}
        <Link to="/permissoes">Permissões</Link>{" | "}
        <Link to="/atribuir-permissoes">Atribuir Permissões</Link>{" | "}
        <Link to="/veiculos">Veículos</Link>
      </p>
    </div>
  );
}

export default Home;

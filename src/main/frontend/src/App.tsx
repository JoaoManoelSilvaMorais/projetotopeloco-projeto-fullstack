import UsuarioList from "./components/UsuarioList";
import PermissaoList from "./components/PermissaoList";
import VeiculoList from "./components/VeiculoList";

function App() {
  return (
    <div>
      <h1>Usuários</h1>
      <UsuarioList />
      <h1>Permissões</h1>
      <PermissaoList />
      <h1>Veículos</h1>
      <VeiculoList />
    </div>
  );
}
export default App;

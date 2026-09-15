import UsuarioList from "./components/UsuarioList";
import VeiculoList from "./components/VeiculoList";
function App() {
  return (
    <div>
      <h1>Usuários cadastrados</h1>
      <UsuarioList />
      <h1>Veículos cadastrados</h1>
      <VeiculoList />
    </div>
  );
}
export default App;
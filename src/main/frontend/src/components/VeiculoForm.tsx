import { useState, type FormEvent } from "react";
import api from "../services/api";
import type { Veiculo } from "../types/Veiculo";

interface VeiculoFormProps {
  onVeiculoSalvo: () => void;
  veiculoEditando?: Veiculo | null;
}

const categorias = ["CONVENCIONAL", "CLASSICO", "MODIFICADO"];

function VeiculoForm({ onVeiculoSalvo, veiculoEditando }: VeiculoFormProps) {
  const [placa, setPlaca] = useState(veiculoEditando?.placa ?? "");
  const [marca, setMarca] = useState(veiculoEditando?.marca ?? "");
  const [modeloBase, setModeloBase] = useState(veiculoEditando?.modeloBase ?? "");
  const [categoria, setCategoria] = useState(veiculoEditando?.categoria ?? "CONVENCIONAL");
  const [especificacaoMotor, setEspecificacaoMotor] = useState(veiculoEditando?.especificacaoMotor ?? "");
  const [anoFabricacao, setAnoFabricacao] = useState(veiculoEditando?.anoFabricacao?.toString() ?? "");

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    const dados = {
      placa,
      marca,
      modeloBase,
      categoria,
      especificacaoMotor,
      anoFabricacao: Number(anoFabricacao),
    };

    if (veiculoEditando) {
      await api.put(`/veiculos/${veiculoEditando.id}`, dados);
    } else {
      await api.post("/veiculos", dados);
    }

    onVeiculoSalvo();
  }

  return (
    <form onSubmit={handleSubmit}>
      <input value={placa} onChange={(event) => setPlaca(event.target.value)} placeholder="Placa" required />
      <input value={marca} onChange={(event) => setMarca(event.target.value)} placeholder="Marca" required />
      <input value={modeloBase} onChange={(event) => setModeloBase(event.target.value)} placeholder="Modelo" required />
      <select value={categoria} onChange={(event) => setCategoria(event.target.value)}>
        {categorias.map((opcao) => <option key={opcao} value={opcao}>{opcao}</option>)}
      </select>
      <input value={especificacaoMotor} onChange={(event) => setEspecificacaoMotor(event.target.value)} placeholder="Especificação do motor" required />
      <input type="number" value={anoFabricacao} onChange={(event) => setAnoFabricacao(event.target.value)} placeholder="Ano de fabricação" required />
      <button type="submit">{veiculoEditando ? "Salvar alterações" : "Cadastrar veículo"}</button>
    </form>
  );
}

export default VeiculoForm;

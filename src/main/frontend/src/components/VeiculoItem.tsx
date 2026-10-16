import { Link } from "react-router-dom";
import type { Veiculo } from "../types/Veiculo";

interface VeiculoItemProps {
  veiculo: Veiculo;
}

function VeiculoItem({ veiculo }: VeiculoItemProps) {
  return <span><Link to={`/veiculos/${veiculo.id}`}><strong>{veiculo.marca} {veiculo.modeloBase}</strong></Link> — Placa: {veiculo.placa} ({veiculo.anoFabricacao})</span>;
}

export default VeiculoItem;

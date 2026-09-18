import { useEffect, useState } from "react";
import api from "../services/api";
import type { Veiculo } from "../types/Veiculo";
import VeiculoForm from "./VeiculoForm";
import VeiculoItem from "./VeiculoItem";

function VeiculoList() {
    const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
    const [editando, setEditando] = useState<Veiculo | null>(null);
    const [erro, setErro] = useState(false);

    function carregarVeiculos() {
        api.get<Veiculo[]>("/veiculos")
            .then((resposta) => setVeiculos(resposta.data))
            .catch(() => setErro(true));
    }

    useEffect(() => {
        carregarVeiculos();
    }, []);

    async function excluir(id: string) {
        await api.delete(`/veiculos/${id}`);
        carregarVeiculos();
    }

    return (
        <div>
            <VeiculoForm
                key={editando?.id ?? "novo"}
                veiculoEditando={editando}
                onVeiculoSalvo={() => {
                    carregarVeiculos();
                    setEditando(null);
                }}
            />
            {erro ? <p>Não foi possível carregar os veículos.</p> : (
                <ul>
                    {veiculos.map((veiculo) => (
                        <li key={veiculo.id}>
                            <VeiculoItem veiculo={veiculo} />
                            <button type="button" onClick={() => setEditando(veiculo)}>Editar veículo</button>
                            <button type="button" onClick={() => excluir(veiculo.id)}>Excluir veículo</button>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default VeiculoList;
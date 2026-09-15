import { useEffect, useState } from "react";
import api from "../services/api";
import type { Veiculo } from "../types/Veiculo";
import VeiculoItem from "./VeiculoItem";

function VeiculoList() {
    const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
    const [erro, setErro] = useState(false);

    useEffect(() => {
        api.get<Veiculo[]>("/veiculos").then((resposta) => {
            if (Array.isArray(resposta.data)) {
                setVeiculos(resposta.data);
            } else {
                setErro(true);
            }
        }).catch(() => {
            setErro(true);
        });
    }, []);

    return (
        <>
            {erro ? <p>Não foi possível carregar os veículos.</p> : (
                <ul>
                    {veiculos.map((veiculo) => (
                        <VeiculoItem key={veiculo.id} veiculo={veiculo} />
                    ))}
                </ul>
            )}
        </>
    );
}

export default VeiculoList;
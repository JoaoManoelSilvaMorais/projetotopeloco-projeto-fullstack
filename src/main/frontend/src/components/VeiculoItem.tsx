import { useState } from "react";
import api from "../services/api";
import type {
    DespesaRecorrente,
    ServicoManutencao,
    Veiculo,
} from "../types/Veiculo";

interface VeiculoItemProps {
    veiculo: Veiculo;
}

function VeiculoItem({ veiculo }: VeiculoItemProps) {
    const [despesas, setDespesas] = useState<DespesaRecorrente[]>([]);
    const [servicos, setServicos] = useState<ServicoManutencao[]>([]);
    const [mostrarDespesas, setMostrarDespesas] = useState(false);
    const [mostrarServicos, setMostrarServicos] = useState(false);

    function alternarDespesas() {
        if (!mostrarDespesas) {
            api.get<DespesaRecorrente[]>("/despesas-recorrentes", {
                params: { veiculoId: veiculo.id },
            }).then((resposta) => setDespesas(resposta.data));
        }
        setMostrarDespesas(!mostrarDespesas);
    }

    function alternarServicos() {
        if (!mostrarServicos) {
            api.get<ServicoManutencao[]>("/servicos-manutencao", {
                params: { veiculoId: veiculo.id },
            }).then((resposta) => setServicos(resposta.data));
        }
        setMostrarServicos(!mostrarServicos);
    }

    return (
        <li>
            <strong>{veiculo.marca} {veiculo.modeloBase}</strong> ({veiculo.placa}) - {veiculo.anoFabricacao}
            <div>
                <button type="button" onClick={alternarDespesas}>
                    {mostrarDespesas ? "Ocultar despesas" : "Exibir despesa recorrente"}
                </button>
                <button type="button" onClick={alternarServicos}>
                    {mostrarServicos ? "Ocultar serviços" : "Exibir serviço de manutenção"}
                </button>
            </div>

            {mostrarDespesas && (
                <div>
                    <h3>Despesas recorrentes</h3>
                    {despesas.length === 0 ? <p>Nenhuma despesa cadastrada.</p> : (
                        <ul>
                            {despesas.map((despesa) => (
                                <li key={despesa.id}>
                                    {despesa.tipo}: R$ {despesa.valorMensal} - vencimento dia {despesa.diaVencimento} ({despesa.statusAtivo ? "ativa" : "inativa"})
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}

            {mostrarServicos && (
                <div>
                    <h3>Serviços de manutenção</h3>
                    {servicos.length === 0 ? <p>Nenhum serviço cadastrado.</p> : (
                        <ul>
                            {servicos.map((servico) => (
                                <li key={servico.id}>
                                    {servico.tipoIntervencao} - {servico.descricao} ({servico.dataRealizacao}) - R$ {(servico.custoPecas ?? 0) + (servico.custoMaoDeObra ?? 0)}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
        </li>
    );
}

export default VeiculoItem;
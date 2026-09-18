import { useState } from "react";
import api from "../services/api";
import type {
    DespesaRecorrente,
    ServicoManutencao,
    Veiculo,
} from "../types/Veiculo";
import DespesaForm from "./DespesaForm";
import ServicoForm from "./ServicoForm";

interface VeiculoItemProps {
    veiculo: Veiculo;
}

function VeiculoItem({ veiculo }: VeiculoItemProps) {
    const [despesas, setDespesas] = useState<DespesaRecorrente[]>([]);
    const [servicos, setServicos] = useState<ServicoManutencao[]>([]);
    const [mostrarDespesas, setMostrarDespesas] = useState(false);
    const [mostrarServicos, setMostrarServicos] = useState(false);
    const [despesaEditando, setDespesaEditando] = useState<DespesaRecorrente | null>(null);
    const [servicoEditando, setServicoEditando] = useState<ServicoManutencao | null>(null);

    function carregarDespesas() {
        api.get<DespesaRecorrente[]>("/despesas-recorrentes", {
            params: { veiculoId: veiculo.id },
        }).then((resposta) => setDespesas(resposta.data));
    }

    function carregarServicos() {
        api.get<ServicoManutencao[]>("/servicos-manutencao", {
            params: { veiculoId: veiculo.id },
        }).then((resposta) => setServicos(resposta.data));
    }

    function alternarDespesas() {
        if (!mostrarDespesas) {
            carregarDespesas();
        }
        setMostrarDespesas(!mostrarDespesas);
    }

    function alternarServicos() {
        if (!mostrarServicos) {
            carregarServicos();
        }
        setMostrarServicos(!mostrarServicos);
    }

    async function excluirDespesa(id: string) {
        await api.delete(`/despesas-recorrentes/${id}`);
        carregarDespesas();
    }

    async function excluirServico(id: string) {
        await api.delete(`/servicos-manutencao/${id}`);
        carregarServicos();
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
                    <DespesaForm
                        key={despesaEditando?.id ?? "nova"}
                        veiculoId={veiculo.id}
                        despesaEditando={despesaEditando}
                        onDespesaSalva={() => {
                            carregarDespesas();
                            setDespesaEditando(null);
                        }}
                    />
                    {despesas.length === 0 ? <p>Nenhuma despesa cadastrada.</p> : (
                        <ul>
                            {despesas.map((despesa) => (
                                <li key={despesa.id}>
                                    {despesa.tipo}: R$ {despesa.valorMensal} - vencimento dia {despesa.diaVencimento} ({despesa.statusAtivo ? "ativa" : "inativa"})
                                    <button type="button" onClick={() => setDespesaEditando(despesa)}>Editar</button>
                                    <button type="button" onClick={() => excluirDespesa(despesa.id)}>Excluir</button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}

            {mostrarServicos && (
                <div>
                    <h3>Serviços de manutenção</h3>
                    <ServicoForm
                        key={servicoEditando?.id ?? "novo"}
                        veiculoId={veiculo.id}
                        servicoEditando={servicoEditando}
                        onServicoSalvo={() => {
                            carregarServicos();
                            setServicoEditando(null);
                        }}
                    />
                    {servicos.length === 0 ? <p>Nenhum serviço cadastrado.</p> : (
                        <ul>
                            {servicos.map((servico) => (
                                <li key={servico.id}>
                                    {servico.tipoIntervencao} - {servico.descricao} ({servico.dataRealizacao}) - R$ {(servico.custoPecas ?? 0) + (servico.custoMaoDeObra ?? 0)}
                                    <button type="button" onClick={() => setServicoEditando(servico)}>Editar</button>
                                    <button type="button" onClick={() => excluirServico(servico.id)}>Excluir</button>
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
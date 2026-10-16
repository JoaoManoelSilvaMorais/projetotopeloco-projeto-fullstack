import type { Permissao } from "../types/Permissao";
import { Link } from "react-router-dom";

interface PermissaoItemProps {
  permissao: Permissao;
}

function PermissaoItem({ permissao }: PermissaoItemProps) {
  return <span><Link to={`/permissoes/${permissao.id}`}><strong>{permissao.nome}</strong></Link> - {permissao.descricao}</span>;
}

export default PermissaoItem;

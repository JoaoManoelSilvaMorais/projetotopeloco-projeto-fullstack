import type { Permissao } from "../types/Permissao";

interface PermissaoItemProps {
  permissao: Permissao;
}

function PermissaoItem({ permissao }: PermissaoItemProps) {
  return (
    <div>
      <strong>{permissao.nome}</strong> - {permissao.descricao}
    </div>
  );
}

export default PermissaoItem;

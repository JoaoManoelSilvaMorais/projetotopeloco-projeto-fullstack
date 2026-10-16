import { Link } from "react-router-dom";
import type { Usuario } from "../types/Usuario";
interface UsuarioItemProps {
    usuario: Usuario;
}
function UsuarioItem({ usuario }: UsuarioItemProps) {
    return <span><Link to={`/usuarios/${usuario.id}`}><strong>{usuario.nome}</strong></Link> ({usuario.username}) — {usuario.email}</span>;
}
export default UsuarioItem;
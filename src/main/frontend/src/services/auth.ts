export interface DadosSessao {
  token: string;
  nome: string;
  username: string;
  permissoes: string[];
  precisaTrocarSenha?: boolean;
}

export const AUTH_STORAGE_KEY = "garagelog_sessao";

export function salvarSessao(dados: DadosSessao): void {
  if (!dados || !dados.token) {
    return;
  }
  localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(dados));
  localStorage.setItem("garagelog_token", dados.token);
}

export function obterSessao(): DadosSessao | null {
  try {
    const raw = localStorage.getItem(AUTH_STORAGE_KEY);
    if (!raw) {
      return null;
    }
    const sessao = JSON.parse(raw) as DadosSessao;
    if (!sessao || typeof sessao.token !== "string" || !sessao.token) {
      return null;
    }
    return sessao;
  } catch {
    return null;
  }
}

export function obterToken(): string | null {
  const sessao = obterSessao();
  if (sessao && sessao.token) {
    return sessao.token;
  }
  return localStorage.getItem("garagelog_token");
}

export function estaAutenticado(): boolean {
  return Boolean(obterToken());
}

export function precisaTrocarSenha(): boolean {
  const sessao = obterSessao();
  return Boolean(sessao?.precisaTrocarSenha);
}

export function marcarSenhaAlterada(): void {
  const sessao = obterSessao();
  if (!sessao) {
    return;
  }
  salvarSessao({ ...sessao, precisaTrocarSenha: false });
}

export function temPapel(papel: string): boolean {
  if (typeof papel !== "string" || !papel.trim()) {
    return false;
  }
  const sessao = obterSessao();
  if (!sessao || !Array.isArray(sessao.permissoes)) {
    return false;
  }
  const alvo = papel.trim().toUpperCase().replace(/^ROLE_/, "");
  return sessao.permissoes.some((p) => {
    if (typeof p !== "string") return false;
    return p.trim().toUpperCase().replace(/^ROLE_/, "") === alvo;
  });
}

export function sair(): void {
  localStorage.removeItem(AUTH_STORAGE_KEY);
  localStorage.removeItem("garagelog_token");
}

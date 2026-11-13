package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security;

/**
 * DTO para solicitação de alteração de senha do usuário autenticado.
 */
public class AlterarSenhaRequestDTO {
    private String senhaAtual;
    private String novaSenha;

    public AlterarSenhaRequestDTO() {
    }

    public AlterarSenhaRequestDTO(String senhaAtual, String novaSenha) {
        this.senhaAtual = senhaAtual;
        this.novaSenha = novaSenha;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }
}

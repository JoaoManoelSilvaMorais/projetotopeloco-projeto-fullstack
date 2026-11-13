package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Security;

import java.util.List;

public class LoginResponseDTO {
    private String token;
    private String nome;
    private String username;
    private List<String> permissoes;
    private boolean precisaTrocarSenha;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, String nome, String username, List<String> permissoes) {
        this(token, nome, username, permissoes, false);
    }

    public LoginResponseDTO(String token, String nome, String username, List<String> permissoes,
            boolean precisaTrocarSenha) {
        this.token = token;
        this.nome = nome;
        this.username = username;
        this.permissoes = permissoes;
        this.precisaTrocarSenha = precisaTrocarSenha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }

    public boolean isPrecisaTrocarSenha() {
        return precisaTrocarSenha;
    }

    public void setPrecisaTrocarSenha(boolean precisaTrocarSenha) {
        this.precisaTrocarSenha = precisaTrocarSenha;
    }
}

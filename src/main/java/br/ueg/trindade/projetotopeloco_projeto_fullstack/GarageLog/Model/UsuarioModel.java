package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;

@Entity
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String username;

    @JsonIgnore
    private String senha;

    private String email;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @JoinTable(
        name = "usuario_permissao",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    @JsonManagedReference
    private Set<PermissaoModel> permissoes = new HashSet<>();
    
    public UsuarioModel() {
    }

    public UsuarioModel(String nome, String username, String senha, String email) {
        this.nome = nome;
        this.username = username;
        this.senha = senha;
        this.email = email;
    }

    //Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public String getUsername() {
        return username;
    }
    public String getSenha() {
        return senha;
    }
    public String getEmail() {
        return email;
    }
    public Set<PermissaoModel> getPermissoes() {
        return permissoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPermissoes(Set<PermissaoModel> permissoes) {
        this.permissoes = permissoes;
    }
}
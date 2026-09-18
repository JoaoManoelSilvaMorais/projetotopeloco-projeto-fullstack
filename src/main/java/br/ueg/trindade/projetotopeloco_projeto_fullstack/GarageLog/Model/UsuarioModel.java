package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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

    public String setNome(String nome) {
        this.nome = nome;
        return nome;
    }
    public String setUsername(String username) {
        this.username = username;
        return username;
    }
    public String setSenha(String senha) {
        this.senha = senha;
        return senha;
    }
    public String setEmail(String email) {
        this.email = email;
        return email;
    }
}
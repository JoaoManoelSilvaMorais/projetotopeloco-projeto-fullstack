package br.ueg.trindade.projetotopeloco_projeto_fullstack.GarageLog.Model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissao")
public class PermissaoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;

    @ManyToMany(mappedBy = "permissoes")
    @JsonBackReference
    private Set<UsuarioModel> usuarios = new HashSet<>();

    public PermissaoModel() {
    }

    public PermissaoModel(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<UsuarioModel> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<UsuarioModel> usuarios) {
        this.usuarios = usuarios;
    }

}

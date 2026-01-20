package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "classe")
public class Classe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe")
    private Integer idClasse;

    @Column(length = 50)
    private String libelle;
    
    @Column(length = 16)
    private String couleur;

    @OneToMany(mappedBy = "classe", fetch = FetchType.LAZY)
    private Set<ClasseCategorie> classeCategories;

    public Classe() {}
    public Classe(Integer idClasse, String libelle) {
        this.idClasse = idClasse;
        this.libelle = libelle;
    }
    public Classe(Integer idClasse, String libelle, String couleur) {
        this.idClasse = idClasse;
        this.libelle = libelle;
        this.couleur = couleur;
    }
    public Integer getIdClasse() { return idClasse; }
    public void setIdClasse(Integer idClasse) { this.idClasse = idClasse; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public Set<ClasseCategorie> getClasseCategories() { return classeCategories; }
    public void setClasseCategories(Set<ClasseCategorie> classeCategories) { this.classeCategories = classeCategories; }
}

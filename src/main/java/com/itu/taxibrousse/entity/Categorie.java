package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie")
    private Integer idCategorie;

    @Column(length = 50)
    private String libelle;

    @Column(length = 20)
    private String couleur; // Hex color like #f472b6 or CSS gradient if desired

    @OneToMany(mappedBy = "categorie", fetch = FetchType.LAZY)
    private Set<ClasseCategorie> classeCategories;

    public Categorie() {}
    public Categorie(Integer idCategorie, String libelle) {
        this.idCategorie = idCategorie;
        this.libelle = libelle;
    }
    public Categorie(Integer idCategorie, String libelle, String couleur) {
        this.idCategorie = idCategorie;
        this.libelle = libelle;
        this.couleur = couleur;
    }
    public Integer getIdCategorie() { return idCategorie; }
    public void setIdCategorie(Integer idCategorie) { this.idCategorie = idCategorie; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }
    public Set<ClasseCategorie> getClasseCategories() { return classeCategories; }
    public void setClasseCategories(Set<ClasseCategorie> classeCategories) { this.classeCategories = classeCategories; }
}

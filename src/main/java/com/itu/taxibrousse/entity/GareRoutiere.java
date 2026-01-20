package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gare_routiere")
public class GareRoutiere {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gare_routiere")
    private Integer idGareRoutiere;
    
    @Column(length = 50)
    private String nom;
    
    @ManyToOne
    @JoinColumn(name = "id_ville", nullable = false)
    private Ville ville;
    
    // Constructors
    public GareRoutiere() {
    }
    
    public GareRoutiere(String nom, Ville ville) {
        this.nom = nom;
        this.ville = ville;
    }
    
    // Getters and Setters
    public Integer getIdGareRoutiere() {
        return idGareRoutiere;
    }
    
    public void setIdGareRoutiere(Integer idGareRoutiere) {
        this.idGareRoutiere = idGareRoutiere;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Ville getVille() {
        return ville;
    }
    
    public void setVille(Ville ville) {
        this.ville = ville;
    }
}

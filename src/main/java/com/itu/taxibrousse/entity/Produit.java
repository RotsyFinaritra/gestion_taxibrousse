package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produit")
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Integer idProduit;
    
    @Column(name = "nom", length = 50)
    private String nom;
    
    @Column(name = "prix", precision = 15, scale = 2)
    private BigDecimal prix;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<HistoriquePrixProduit> historiquePrix;
    
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<VenteDetails> venteDetails;
    
    // Constructors
    public Produit() {}
    
    public Produit(String nom, BigDecimal prix) {
        this.nom = nom;
        this.prix = prix;
    }
    
    // Getters and Setters
    public Integer getIdProduit() {
        return idProduit;
    }
    
    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public BigDecimal getPrix() {
        return prix;
    }
    
    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
    
    public List<HistoriquePrixProduit> getHistoriquePrix() {
        return historiquePrix;
    }
    
    public void setHistoriquePrix(List<HistoriquePrixProduit> historiquePrix) {
        this.historiquePrix = historiquePrix;
    }
    
    public List<VenteDetails> getVenteDetails() {
        return venteDetails;
    }
    
    public void setVenteDetails(List<VenteDetails> venteDetails) {
        this.venteDetails = venteDetails;
    }
}

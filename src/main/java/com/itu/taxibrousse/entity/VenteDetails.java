package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vente_details")
public class VenteDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vente_details")
    private Integer idVenteDetails;
    
    @Column(name = "date_")
    private LocalDate date;
    
    @Column(name = "quantite")
    private Integer quantite;
    
    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;
    
    @ManyToOne
    @JoinColumn(name = "id_vente_produit", nullable = false)
    private VenteProduit venteProduit;
    
    // Constructors
    public VenteDetails() {}
    
    public VenteDetails(LocalDate date, Integer quantite, Produit produit, VenteProduit venteProduit) {
        this.date = date;
        this.quantite = quantite;
        this.produit = produit;
        this.venteProduit = venteProduit;
    }
    
    // Getters and Setters
    public Integer getIdVenteDetails() {
        return idVenteDetails;
    }
    
    public void setIdVenteDetails(Integer idVenteDetails) {
        this.idVenteDetails = idVenteDetails;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public VenteProduit getVenteProduit() {
        return venteProduit;
    }
    
    public void setVenteProduit(VenteProduit venteProduit) {
        this.venteProduit = venteProduit;
    }
}

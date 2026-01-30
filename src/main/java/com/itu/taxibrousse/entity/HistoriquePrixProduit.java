package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historique_prix_produit")
public class HistoriquePrixProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_prix_produit")
    private Integer idHistoriquePrixProduit;
    
    @Column(name = "montant", precision = 15, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "date_")
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;
    
    // Constructors
    public HistoriquePrixProduit() {}
    
    public HistoriquePrixProduit(BigDecimal montant, LocalDate date, Produit produit) {
        this.montant = montant;
        this.date = date;
        this.produit = produit;
    }
    
    // Getters and Setters
    public Integer getIdHistoriquePrixProduit() {
        return idHistoriquePrixProduit;
    }
    
    public void setIdHistoriquePrixProduit(Integer idHistoriquePrixProduit) {
        this.idHistoriquePrixProduit = idHistoriquePrixProduit;
    }
    
    public BigDecimal getMontant() {
        return montant;
    }
    
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}

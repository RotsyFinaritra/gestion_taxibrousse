package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vente_produit")
public class VenteProduit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vente_produit")
    private Integer idVenteProduit;
    
    @Column(name = "quantite")
    private Integer quantite;
    
    @Column(name = "date_")
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "id_voyage_vehicule", nullable = false)
    private VoyageVehicule voyageVehicule;
    
    @OneToMany(mappedBy = "venteProduit", cascade = CascadeType.ALL)
    private List<VenteDetails> venteDetails;
    
    // Constructors
    public VenteProduit() {}
    
    public VenteProduit(Integer quantite, LocalDate date, VoyageVehicule voyageVehicule) {
        this.quantite = quantite;
        this.date = date;
        this.voyageVehicule = voyageVehicule;
    }
    
    // Getters and Setters
    public Integer getIdVenteProduit() {
        return idVenteProduit;
    }
    
    public void setIdVenteProduit(Integer idVenteProduit) {
        this.idVenteProduit = idVenteProduit;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public VoyageVehicule getVoyageVehicule() {
        return voyageVehicule;
    }
    
    public void setVoyageVehicule(VoyageVehicule voyageVehicule) {
        this.voyageVehicule = voyageVehicule;
    }
    
    public List<VenteDetails> getVenteDetails() {
        return venteDetails;
    }
    
    public void setVenteDetails(List<VenteDetails> venteDetails) {
        this.venteDetails = venteDetails;
    }
}

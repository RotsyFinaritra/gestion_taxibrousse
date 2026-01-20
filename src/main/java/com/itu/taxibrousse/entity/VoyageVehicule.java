package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "voyage_vehicule")
public class VoyageVehicule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voyage_vehicule")
    private Integer idVoyageVehicule;
    
    @Column
    private Double prix;
    
    @ManyToOne
    @JoinColumn(name = "id_voyage", nullable = false)
    private Voyage voyage;
    
    @ManyToOne
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;
    
    // Constructors
    public VoyageVehicule() {
    }
    
    public VoyageVehicule(Double prix, Voyage voyage, Vehicule vehicule) {
        this.prix = prix;
        this.voyage = voyage;
        this.vehicule = vehicule;
    }
    
    // Getters and Setters
    public Integer getIdVoyageVehicule() {
        return idVoyageVehicule;
    }
    
    public void setIdVoyageVehicule(Integer idVoyageVehicule) {
        this.idVoyageVehicule = idVoyageVehicule;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public Voyage getVoyage() {
        return voyage;
    }
    
    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }
    
    public Vehicule getVehicule() {
        return vehicule;
    }
    
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }
}

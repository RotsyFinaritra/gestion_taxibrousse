package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicule")
public class Vehicule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicule")
    private Integer idVehicule;
    
    @Column(length = 50)
    private String modele;
    
    @Column(length = 50)
    private String marque;
    
    @Column(length = 50)
    private String immatriculation;
    
    @Column
    private Integer capacite;
    
    // Constructors
    public Vehicule() {
    }
    
    public Vehicule(String modele, String marque, String immatriculation, Integer capacite) {
        this.modele = modele;
        this.marque = marque;
        this.immatriculation = immatriculation;
        this.capacite = capacite;
    }
    
    // Getters and Setters
    public Integer getIdVehicule() {
        return idVehicule;
    }
    
    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }
    
    public String getModele() {
        return modele;
    }
    
    public void setModele(String modele) {
        this.modele = modele;
    }
    
    public String getMarque() {
        return marque;
    }
    
    public void setMarque(String marque) {
        this.marque = marque;
    }
    
    public String getImmatriculation() {
        return immatriculation;
    }
    
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }
    
    public Integer getCapacite() {
        return capacite;
    }
    
    public void setCapacite(Integer capacite) {
        this.capacite = capacite;
    }
}

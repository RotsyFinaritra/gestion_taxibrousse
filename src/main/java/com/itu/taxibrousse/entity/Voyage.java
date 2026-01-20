package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "voyage")
public class Voyage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_voyage")
    private Integer idVoyage;
    
    @Column(name = "date_depart")
    private LocalDate dateDepart;
    
    @Column
    private Double prix;
    
    @ManyToOne
    @JoinColumn(name = "id_heure_depart", nullable = false)
    private HeureDepart heureDepart;
    
    @ManyToOne
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;
    
    // Constructors
    public Voyage() {
    }
    
    public Voyage(LocalDate dateDepart, Double prix, HeureDepart heureDepart, Trajet trajet) {
        this.dateDepart = dateDepart;
        this.prix = prix;
        this.heureDepart = heureDepart;
        this.trajet = trajet;
    }
    
    // Getters and Setters
    public Integer getIdVoyage() {
        return idVoyage;
    }
    
    public void setIdVoyage(Integer idVoyage) {
        this.idVoyage = idVoyage;
    }
    
    public LocalDate getDateDepart() {
        return dateDepart;
    }
    
    public void setDateDepart(LocalDate dateDepart) {
        this.dateDepart = dateDepart;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public HeureDepart getHeureDepart() {
        return heureDepart;
    }
    
    public void setHeureDepart(HeureDepart heureDepart) {
        this.heureDepart = heureDepart;
    }
    
    public Trajet getTrajet() {
        return trajet;
    }
    
    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}

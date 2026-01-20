package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "trajet")
public class Trajet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trajet")
    private Integer idTrajet;
    
    @Column
    private Double prix;
    
    @Column
    private Double distance;
    
    @ManyToOne
    @JoinColumn(name = "id_gare_routiere_depart", nullable = false)
    private GareRoutiere gareRoutiereDepart;
    
    @ManyToOne
    @JoinColumn(name = "id_gare_routiere_arrivee", nullable = false)
    private GareRoutiere gareRoutiereArrivee;
    
    // Constructors
    public Trajet() {
    }
    
    public Trajet(Double prix, Double distance, GareRoutiere gareRoutiereDepart, GareRoutiere gareRoutiereArrivee) {
        this.prix = prix;
        this.distance = distance;
        this.gareRoutiereDepart = gareRoutiereDepart;
        this.gareRoutiereArrivee = gareRoutiereArrivee;
    }
    
    // Getters and Setters
    public Integer getIdTrajet() {
        return idTrajet;
    }
    
    public void setIdTrajet(Integer idTrajet) {
        this.idTrajet = idTrajet;
    }
    
    public Double getPrix() {
        return prix;
    }
    
    public void setPrix(Double prix) {
        this.prix = prix;
    }
    
    public Double getDistance() {
        return distance;
    }
    
    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    public GareRoutiere getGareRoutiereDepart() {
        return gareRoutiereDepart;
    }
    
    public void setGareRoutiereDepart(GareRoutiere gareRoutiereDepart) {
        this.gareRoutiereDepart = gareRoutiereDepart;
    }
    
    public GareRoutiere getGareRoutiereArrivee() {
        return gareRoutiereArrivee;
    }
    
    public void setGareRoutiereArrivee(GareRoutiere gareRoutiereArrivee) {
        this.gareRoutiereArrivee = gareRoutiereArrivee;
    }
}

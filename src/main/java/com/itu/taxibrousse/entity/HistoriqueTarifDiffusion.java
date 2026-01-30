package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historique_tarif_diffusion")
public class HistoriqueTarifDiffusion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historique_tarif")
    private Integer idHistoriqueTarif;
    
    @Column(name = "tarif", precision = 15, scale = 2, nullable = false)
    private BigDecimal tarif;
    
    @Column(name = "date_", nullable = false)
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;
    
    // Constructeurs
    public HistoriqueTarifDiffusion() {}
    
    public HistoriqueTarifDiffusion(BigDecimal tarif, LocalDate date, Societe societe) {
        this.tarif = tarif;
        this.date = date;
        this.societe = societe;
    }
    
    // Getters et Setters
    public Integer getIdHistoriqueTarif() {
        return idHistoriqueTarif;
    }
    
    public void setIdHistoriqueTarif(Integer idHistoriqueTarif) {
        this.idHistoriqueTarif = idHistoriqueTarif;
    }
    
    public BigDecimal getTarif() {
        return tarif;
    }
    
    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public Societe getSociete() {
        return societe;
    }
    
    public void setSociete(Societe societe) {
        this.societe = societe;
    }
    
    // toString
    @Override
    public String toString() {
        return "HistoriqueTarifDiffusion{" +
                "idHistoriqueTarif=" + idHistoriqueTarif +
                ", tarif=" + tarif +
                ", date=" + date +
                ", societe=" + (societe != null ? societe.getNom() : "null") +
                '}';
    }
}
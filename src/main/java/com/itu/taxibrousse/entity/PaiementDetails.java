package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement_details")
public class PaiementDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement_details")
    private Integer idPaiementDetails;

    @ManyToOne
    @JoinColumn(name = "id_paiement", nullable = false)
    private PaiementDiffusion paiementDiffusion;

    @Column(name = "montant", precision = 15, scale = 2)
    private BigDecimal montant;

    @ManyToOne
    @JoinColumn(name = "id_diffusion_details", nullable = false)
    private DiffusionPubDetails diffusionDetails;

    @Column(name = "date")
    private LocalDate date;

    // Constructors
    public PaiementDetails() {}

    public PaiementDetails(PaiementDiffusion paiementDiffusion, BigDecimal montant, 
                          DiffusionPubDetails diffusionDetails, LocalDate date) {
        this.paiementDiffusion = paiementDiffusion;
        this.montant = montant;
        this.diffusionDetails = diffusionDetails;
        this.date = date;
    }

    // Getters and Setters
    public Integer getIdPaiementDetails() {
        return idPaiementDetails;
    }

    public void setIdPaiementDetails(Integer idPaiementDetails) {
        this.idPaiementDetails = idPaiementDetails;
    }

    public PaiementDiffusion getPaiementDiffusion() {
        return paiementDiffusion;
    }

    public void setPaiementDiffusion(PaiementDiffusion paiementDiffusion) {
        this.paiementDiffusion = paiementDiffusion;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public DiffusionPubDetails getDiffusionDetails() {
        return diffusionDetails;
    }

    public void setDiffusionDetails(DiffusionPubDetails diffusionDetails) {
        this.diffusionDetails = diffusionDetails;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
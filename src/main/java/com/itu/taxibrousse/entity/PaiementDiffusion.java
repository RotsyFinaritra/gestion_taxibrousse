package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiement_diffusion")
public class PaiementDiffusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement_diffusion")
    private Integer idPaiementDiffusion;

    @Column(name = "montant", precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "mois_annee_a_payer")
    private LocalDate moisAnneeAPayer;

    @ManyToOne
    @JoinColumn(name = "id_diffusion_pub", nullable = false)
    private DiffusionPublicitaire diffusionPublicitaire;

    @OneToMany(mappedBy = "paiementDiffusion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<PaiementDetails> details;

    public PaiementDiffusion() {
    }

    public Integer getIdPaiementDiffusion() {
        return idPaiementDiffusion;
    }

    public void setIdPaiementDiffusion(Integer idPaiementDiffusion) {
        this.idPaiementDiffusion = idPaiementDiffusion;
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

    public LocalDate getMoisAnneeAPayer() {
        return moisAnneeAPayer;
    }

    public void setMoisAnneeAPayer(LocalDate moisAnneeAPayer) {
        this.moisAnneeAPayer = moisAnneeAPayer;
    }

    public DiffusionPublicitaire getDiffusionPublicitaire() {
        return diffusionPublicitaire;
    }

    public void setDiffusionPublicitaire(DiffusionPublicitaire diffusionPublicitaire) {
        this.diffusionPublicitaire = diffusionPublicitaire;
    }

    public java.util.List<PaiementDetails> getDetails() {
        return details;
    }

    public void setDetails(java.util.List<PaiementDetails> details) {
        this.details = details;
    }
}

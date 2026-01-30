package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "diffusion_publicitaire")
public class DiffusionPublicitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diffusion_publicitaire")
    private Integer idDiffusionPublicitaire;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_cloturage")
    private LocalDate dateCloturage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @OneToMany(mappedBy = "diffusionPublicitaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DiffusionPubDetails> details;

    // Constructors
    public DiffusionPublicitaire() {}

    public DiffusionPublicitaire(LocalDate dateDebut, LocalDate dateCloturage, Societe societe) {
        this.dateDebut = dateDebut;
        this.dateCloturage = dateCloturage;
        this.societe = societe;
    }

    // Getters and Setters
    public Integer getIdDiffusionPublicitaire() {
        return idDiffusionPublicitaire;
    }

    public void setIdDiffusionPublicitaire(Integer idDiffusionPublicitaire) {
        this.idDiffusionPublicitaire = idDiffusionPublicitaire;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateCloturage() {
        return dateCloturage;
    }

    public void setDateCloturage(LocalDate dateCloturage) {
        this.dateCloturage = dateCloturage;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public List<DiffusionPubDetails> getDetails() {
        return details;
    }

    public void setDetails(List<DiffusionPubDetails> details) {
        this.details = details;
    }
}

package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "societe")
public class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_societe")
    private Integer idSociete;

    @Column(name = "nom", length = 50)
    private String nom;

    @Column(name = "contact", length = 50)
    private String contact;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "tarif_par_diffusion", precision = 15, scale = 2)
    private BigDecimal tarifParDiffusion;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @OneToMany(mappedBy = "societe", fetch = FetchType.LAZY)
    private Set<VideoPublicitaire> videosPublicitaires;

    // Constructors
    public Societe() {}

    public Societe(String nom, String contact, String email, BigDecimal tarifParDiffusion, LocalDate dateCreation) {
        this.nom = nom;
        this.contact = contact;
        this.email = email;
        this.tarifParDiffusion = tarifParDiffusion;
        this.dateCreation = dateCreation;
    }

    // Getters and Setters
    public Integer getIdSociete() {
        return idSociete;
    }

    public void setIdSociete(Integer idSociete) {
        this.idSociete = idSociete;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getTarifParDiffusion() {
        return tarifParDiffusion;
    }

    public void setTarifParDiffusion(BigDecimal tarifParDiffusion) {
        this.tarifParDiffusion = tarifParDiffusion;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Set<VideoPublicitaire> getVideosPublicitaires() {
        return videosPublicitaires;
    }

    public void setVideosPublicitaires(Set<VideoPublicitaire> videosPublicitaires) {
        this.videosPublicitaires = videosPublicitaires;
    }
}
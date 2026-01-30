package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "video_publicitaire")
public class VideoPublicitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video_publicitaire")
    private Integer idVideoPublicitaire;

    @Column(name = "titre", length = 50)
    private String titre;

    @Column(name = "url_fichier", length = 50)
    private String urlFichier;

    @Column(name = "date_ajout")
    private LocalDate dateAjout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_societe", nullable = false)
    private Societe societe;

    @OneToMany(mappedBy = "videoPublicitaire", fetch = FetchType.LAZY)
    private Set<DiffusionPubDetails> diffusionPubDetails;

    // Constructors
    public VideoPublicitaire() {}

    public VideoPublicitaire(String titre, String urlFichier, LocalDate dateAjout, Societe societe) {
        this.titre = titre;
        this.urlFichier = urlFichier;
        this.dateAjout = dateAjout;
        this.societe = societe;
    }

    // Getters and Setters
    public Integer getIdVideoPublicitaire() {
        return idVideoPublicitaire;
    }

    public void setIdVideoPublicitaire(Integer idVideoPublicitaire) {
        this.idVideoPublicitaire = idVideoPublicitaire;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUrlFichier() {
        return urlFichier;
    }

    public void setUrlFichier(String urlFichier) {
        this.urlFichier = urlFichier;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Societe getSociete() {
        return societe;
    }

    public void setSociete(Societe societe) {
        this.societe = societe;
    }

    public Set<DiffusionPubDetails> getDiffusionPubDetails() {
        return diffusionPubDetails;
    }

    public void setDiffusionPubDetails(Set<DiffusionPubDetails> diffusionPubDetails) {
        this.diffusionPubDetails = diffusionPubDetails;
    }
}
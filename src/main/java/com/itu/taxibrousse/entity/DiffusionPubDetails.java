package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "diffusion_pub_details")
public class DiffusionPubDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diffusion_pub_details")
    private Integer idDiffusionPubDetails;

    @Column(name = "date_diffusion")
    private LocalDateTime dateDiffusion;

    @Column(name = "nb_diffusion")
    private Integer nbDiffusion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diffusion_publicitaire", nullable = false)
    private DiffusionPublicitaire diffusionPublicitaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voyage_vehicule", nullable = false)
    private VoyageVehicule voyageVehicule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video_publicitaire", nullable = false)
    private VideoPublicitaire videoPublicitaire;

    // Constructors
    public DiffusionPubDetails() {}

    public DiffusionPubDetails(LocalDateTime dateDiffusion, Integer nbDiffusion, 
                               DiffusionPublicitaire diffusionPublicitaire, 
                               VoyageVehicule voyageVehicule, 
                               VideoPublicitaire videoPublicitaire) {
        this.dateDiffusion = dateDiffusion;
        this.nbDiffusion = nbDiffusion;
        this.diffusionPublicitaire = diffusionPublicitaire;
        this.voyageVehicule = voyageVehicule;
        this.videoPublicitaire = videoPublicitaire;
    }

    // Getters and Setters
    public Integer getIdDiffusionPubDetails() {
        return idDiffusionPubDetails;
    }

    public void setIdDiffusionPubDetails(Integer idDiffusionPubDetails) {
        this.idDiffusionPubDetails = idDiffusionPubDetails;
    }

    public LocalDateTime getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(LocalDateTime dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public Integer getNbDiffusion() {
        return nbDiffusion;
    }

    public void setNbDiffusion(Integer nbDiffusion) {
        this.nbDiffusion = nbDiffusion;
    }

    public DiffusionPublicitaire getDiffusionPublicitaire() {
        return diffusionPublicitaire;
    }

    public void setDiffusionPublicitaire(DiffusionPublicitaire diffusionPublicitaire) {
        this.diffusionPublicitaire = diffusionPublicitaire;
    }

    public VoyageVehicule getVoyageVehicule() {
        return voyageVehicule;
    }

    public void setVoyageVehicule(VoyageVehicule voyageVehicule) {
        this.voyageVehicule = voyageVehicule;
    }

    public VideoPublicitaire getVideoPublicitaire() {
        return videoPublicitaire;
    }

    public void setVideoPublicitaire(VideoPublicitaire videoPublicitaire) {
        this.videoPublicitaire = videoPublicitaire;
    }

    // Calcul du chiffre d'affaires estimé pour cette diffusion (tarif * nbDiffusion)
    public BigDecimal getEstimatedCa() {
        if (this.videoPublicitaire == null) return BigDecimal.ZERO;
        var soc = this.videoPublicitaire.getSociete();
        if (soc == null || soc.getTarifParDiffusion() == null) return BigDecimal.ZERO;
        int nb = (this.nbDiffusion != null) ? this.nbDiffusion : 1;
        return soc.getTarifParDiffusion().multiply(BigDecimal.valueOf(nb));
    }
}

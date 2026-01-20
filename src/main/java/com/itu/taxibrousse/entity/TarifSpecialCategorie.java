package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tarif_special_categorie")
public class TarifSpecialCategorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif_sepcial_categorie")
    private Integer idTarifSpecialCategorie;

    /**
     * Remise exprimée en pourcentage (ex: 12.50 => 12.5%).
     * Stored as NUMERIC(precision, scale) in DB. Using precision=5, scale=2 allows up to 999.99%.
     */
    @Column(name = "remise", precision = 5, scale = 2)
    private BigDecimal remise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe_categorie", nullable = false)
    private ClasseCategorie classeCategorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie", nullable = false)
    private Categorie categorie;

    public TarifSpecialCategorie() {
    }

    public Integer getIdTarifSpecialCategorie() {
        return idTarifSpecialCategorie;
    }

    public void setIdTarifSpecialCategorie(Integer idTarifSpecialCategorie) {
        this.idTarifSpecialCategorie = idTarifSpecialCategorie;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public ClasseCategorie getClasseCategorie() {
        return classeCategorie;
    }

    public void setClasseCategorie(ClasseCategorie classeCategorie) {
        this.classeCategorie = classeCategorie;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}

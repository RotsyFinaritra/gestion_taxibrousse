package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tarif_classe_categorie_trajet")
public class TarifClasseCategorieTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif_classe_categorie_trajet")
    private Integer idTarifClasseCategorieTrajet;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date_")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe_categorie", nullable = false)
    private ClasseCategorie classeCategorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    public TarifClasseCategorieTrajet() {
    }

    public Integer getIdTarifClasseCategorieTrajet() {
        return idTarifClasseCategorieTrajet;
    }

    public void setIdTarifClasseCategorieTrajet(Integer idTarifClasseCategorieTrajet) {
        this.idTarifClasseCategorieTrajet = idTarifClasseCategorieTrajet;
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

    public ClasseCategorie getClasseCategorie() {
        return classeCategorie;
    }

    public void setClasseCategorie(ClasseCategorie classeCategorie) {
        this.classeCategorie = classeCategorie;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}

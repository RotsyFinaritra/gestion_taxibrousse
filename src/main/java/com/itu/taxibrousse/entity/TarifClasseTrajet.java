package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tarif_classe_trajet")
public class TarifClasseTrajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarif_classe_trajet")
    private Integer idTarifClasseTrajet;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date_")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trajet", nullable = false)
    private Trajet trajet;

    public TarifClasseTrajet() {
    }

    public Integer getIdTarifClasseTrajet() {
        return idTarifClasseTrajet;
    }

    public void setIdTarifClasseTrajet(Integer idTarifClasseTrajet) {
        this.idTarifClasseTrajet = idTarifClasseTrajet;
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

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }
}

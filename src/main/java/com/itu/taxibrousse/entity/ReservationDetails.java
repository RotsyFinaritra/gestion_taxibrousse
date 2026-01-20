package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import com.itu.taxibrousse.entity.ClasseCategorie;

@Entity
@Table(name = "reservation_details")
public class ReservationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation_details")
    private Integer idReservationDetails;

    @Column(name = "num_place")
    private Integer numPlace;

    @Column
    private Double montant;

    @Column
    private Integer etat;

    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "id_classe_categorie")
    private ClasseCategorie classeCategorie;

    // Constructors
    public ReservationDetails() {
    }

    public ReservationDetails(Integer numPlace, Double montant, Integer etat, Reservation reservation) {
        this.numPlace = numPlace;
        this.montant = montant;
        this.etat = etat;
        this.reservation = reservation;
    }

    // Getters and Setters
    public Integer getIdReservationDetails() {
        return idReservationDetails;
    }

    public void setIdReservationDetails(Integer idReservationDetails) {
        this.idReservationDetails = idReservationDetails;
    }

    public Integer getNumPlace() {
        return numPlace;
    }

    public void setNumPlace(Integer numPlace) {
        this.numPlace = numPlace;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ClasseCategorie getClasseCategorie() {
        return classeCategorie;
    }

    public void setClasseCategorie(ClasseCategorie classeCategorie) {
        this.classeCategorie = classeCategorie;
    }

    public static String getEtatLibelle(Integer etat) {
        if (etat == null)
            return "<span class='badge bg-secondary'>Inconnu</span>";
        switch (etat) {
            case 1:
                return "<span class='badge bg-warning text-dark'>En attente</span>";
            case 10:
                return "<span class='badge bg-success'>Confirmée</span>";
            case 0:
                return "<span class='badge bg-danger'>Annulée</span>";
            case -1:
                return "<span class='badge bg-dark'>Refusée</span>";
            default:
                return "<span class='badge bg-secondary'>Inconnu</span>";
        }
    }
}

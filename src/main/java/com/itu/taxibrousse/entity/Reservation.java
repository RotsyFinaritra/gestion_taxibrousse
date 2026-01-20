package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reservation")
    private Integer idReservation;

    @Column(length = 50)
    private String nom;

    @Column(length = 50)
    private String contact;

    @Column(name = "nombre_place")
    private Integer nombrePlace;

    @Column(name = "date_")
    private LocalDateTime date;

    @Column
    private Integer etat;

    @ManyToOne
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_voyage_vehicule", nullable = false)
    private VoyageVehicule voyageVehicule;

    // Constructors
    public Reservation() {
    }

    public Reservation(String nom, String contact, Integer nombrePlace, LocalDateTime date,
            Integer etat, Client client, VoyageVehicule voyageVehicule) {
        this.nom = nom;
        this.contact = contact;
        this.nombrePlace = nombrePlace;
        this.date = date;
        this.etat = etat;
        this.client = client;
        this.voyageVehicule = voyageVehicule;
    }

    // Getters and Setters
    public Integer getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(Integer idReservation) {
        this.idReservation = idReservation;
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

    public Integer getNombrePlace() {
        return nombrePlace;
    }

    public void setNombrePlace(Integer nombrePlace) {
        this.nombrePlace = nombrePlace;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getEtat() {
        return etat;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public VoyageVehicule getVoyageVehicule() {
        return voyageVehicule;
    }

    public void setVoyageVehicule(VoyageVehicule voyageVehicule) {
        this.voyageVehicule = voyageVehicule;
    }
}

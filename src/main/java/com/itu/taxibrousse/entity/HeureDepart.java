package com.itu.taxibrousse.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "heure_depart")
public class HeureDepart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_heure_depart")
    private Integer idHeureDepart;
    
    @Column
    private LocalTime heure;
    
    // Constructors
    public HeureDepart() {
    }
    
    public HeureDepart(LocalTime heure) {
        this.heure = heure;
    }
    
    // Getters and Setters
    public Integer getIdHeureDepart() {
        return idHeureDepart;
    }
    
    public void setIdHeureDepart(Integer idHeureDepart) {
        this.idHeureDepart = idHeureDepart;
    }
    
    public LocalTime getHeure() {
        return heure;
    }
    
    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }
}

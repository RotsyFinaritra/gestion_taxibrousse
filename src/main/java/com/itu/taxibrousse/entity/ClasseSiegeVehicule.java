package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classe_siege_vehicule")
public class ClasseSiegeVehicule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe_siege_vehicule")
    private Integer idClasseSiegeVehicule;

    @Column(name = "num_place")
    private Integer numPlace;

    @ManyToOne
    @JoinColumn(name = "id_vehicule", nullable = false)
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    public ClasseSiegeVehicule() {}

    public ClasseSiegeVehicule(Integer idClasseSiegeVehicule, Integer numPlace, Vehicule vehicule, Classe classe) {
        this.idClasseSiegeVehicule = idClasseSiegeVehicule;
        this.numPlace = numPlace;
        this.vehicule = vehicule;
        this.classe = classe;
    }

    public Integer getIdClasseSiegeVehicule() { return idClasseSiegeVehicule; }
    public void setIdClasseSiegeVehicule(Integer idClasseSiegeVehicule) { this.idClasseSiegeVehicule = idClasseSiegeVehicule; }

    public Integer getNumPlace() { return numPlace; }
    public void setNumPlace(Integer numPlace) { this.numPlace = numPlace; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }
}

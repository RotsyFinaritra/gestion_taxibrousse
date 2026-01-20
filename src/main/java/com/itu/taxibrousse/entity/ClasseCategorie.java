package com.itu.taxibrousse.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "classe_categorie")
public class ClasseCategorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_classe_categorie")
    private Integer idClasseCategorie;

    @ManyToOne
    @JoinColumn(name = "id_categorie", nullable = false)
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "id_classe", nullable = false)
    private Classe classe;

    public ClasseCategorie() {}
    public ClasseCategorie(Integer idClasseCategorie, Categorie categorie, Classe classe) {
        this.idClasseCategorie = idClasseCategorie;
        this.categorie = categorie;
        this.classe = classe;
    }
    public Integer getIdClasseCategorie() { return idClasseCategorie; }
    public void setIdClasseCategorie(Integer idClasseCategorie) { this.idClasseCategorie = idClasseCategorie; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }
}

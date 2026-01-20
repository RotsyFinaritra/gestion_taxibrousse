package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrajetRepository extends JpaRepository<Trajet, Integer> {
    List<Trajet> findByGareRoutiereDepart_IdGareRoutiere(Integer idGareRoutiereDepart);
    List<Trajet> findByGareRoutiereArrivee_IdGareRoutiere(Integer idGareRoutiereArrivee);
    List<Trajet> findByGareRoutiereDepart_Ville_IdVilleAndGareRoutiereArrivee_Ville_IdVille(Integer idVilleDepart, Integer idVilleArrivee);
}

package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VideoPublicitaire;
import com.itu.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VideoPublicitaireRepository extends JpaRepository<VideoPublicitaire, Integer> {
    
    List<VideoPublicitaire> findBySociete(Societe societe);
    
    List<VideoPublicitaire> findBySocieteIdSociete(Integer idSociete);
    
    @Query("SELECT v FROM VideoPublicitaire v WHERE v.titre LIKE %:titre%")
    List<VideoPublicitaire> findByTitreContaining(@Param("titre") String titre);
    
    @Query("SELECT v FROM VideoPublicitaire v WHERE v.dateAjout BETWEEN :dateDebut AND :dateFin")
    List<VideoPublicitaire> findByDateAjoutBetween(@Param("dateDebut") LocalDate dateDebut, 
                                                   @Param("dateFin") LocalDate dateFin);
    
    @Query("SELECT v FROM VideoPublicitaire v ORDER BY v.dateAjout DESC")
    List<VideoPublicitaire> findAllOrderByDateAjoutDesc();
}
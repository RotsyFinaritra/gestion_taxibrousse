package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.HistoriqueTarifDiffusion;
import com.itu.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoriqueTarifDiffusionRepository extends JpaRepository<HistoriqueTarifDiffusion, Integer> {
    
    /**
     * Trouve tous les historiques de tarifs pour une société donnée
     */
    List<HistoriqueTarifDiffusion> findBySocieteOrderByDateDesc(Societe societe);
    
    /**
     * Trouve tous les historiques de tarifs pour une société donnée par son ID
     */
    @Query("SELECT h FROM HistoriqueTarifDiffusion h WHERE h.societe.idSociete = :idSociete ORDER BY h.date DESC")
    List<HistoriqueTarifDiffusion> findBySocieteIdOrderByDateDesc(@Param("idSociete") Integer idSociete);
    
    /**
     * Trouve l'historique de tarif le plus récent pour une société à une date donnée
     */
    @Query("SELECT h FROM HistoriqueTarifDiffusion h WHERE h.societe.idSociete = :idSociete AND h.date <= :date ORDER BY h.date DESC")
    List<HistoriqueTarifDiffusion> findTarifValidePourSocieteEtDate(@Param("idSociete") Integer idSociete, @Param("date") LocalDate date);
    
    /**
     * Trouve tous les historiques entre deux dates
     */
    @Query("SELECT h FROM HistoriqueTarifDiffusion h WHERE h.date BETWEEN :dateDebut AND :dateFin ORDER BY h.date DESC, h.societe.nom")
    List<HistoriqueTarifDiffusion> findByDateBetweenOrderByDateDescSocieteNom(
            @Param("dateDebut") LocalDate dateDebut, 
            @Param("dateFin") LocalDate dateFin
    );
    
    /**
     * Trouve les derniers tarifs pour chaque société
     */
    @Query("SELECT h FROM HistoriqueTarifDiffusion h WHERE h.date = (" +
           "SELECT MAX(h2.date) FROM HistoriqueTarifDiffusion h2 WHERE h2.societe.idSociete = h.societe.idSociete" +
           ") ORDER BY h.societe.nom")
    List<HistoriqueTarifDiffusion> findDernierTarifParSociete();
    
    /**
     * Vérifie si un tarif existe pour une société à une date donnée
     */
    @Query("SELECT COUNT(h) > 0 FROM HistoriqueTarifDiffusion h WHERE h.societe.idSociete = :idSociete AND h.date = :date")
    boolean existsBySocieteIdAndDate(@Param("idSociete") Integer idSociete, @Param("date") LocalDate date);
}
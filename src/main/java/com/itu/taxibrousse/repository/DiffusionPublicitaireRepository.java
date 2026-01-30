package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.DiffusionPublicitaire;
import com.itu.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiffusionPublicitaireRepository extends JpaRepository<DiffusionPublicitaire, Integer> {
    
    List<DiffusionPublicitaire> findBySociete(Societe societe);
    
    List<DiffusionPublicitaire> findBySociete_IdSociete(Integer idSociete);
    
    List<DiffusionPublicitaire> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);
    
    List<DiffusionPublicitaire> findByDateCloturageIsNull();
    
    @Query("SELECT d FROM DiffusionPublicitaire d WHERE d.dateDebut <= :date AND (d.dateCloturage IS NULL OR d.dateCloturage >= :date)")
    List<DiffusionPublicitaire> findActivesADate(@Param("date") LocalDate date);
}
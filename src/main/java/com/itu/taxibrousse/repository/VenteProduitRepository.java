package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.entity.VoyageVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Integer> {
    
    List<VenteProduit> findByVoyageVehiculeOrderByDateDesc(VoyageVehicule voyageVehicule);
    
    List<VenteProduit> findByVoyageVehicule_IdVoyageVehiculeOrderByDateDesc(Integer idVoyageVehicule);
    
    List<VenteProduit> findByDateBetween(LocalDate dateDebut, LocalDate dateFin);
    
    @Query("SELECT v FROM VenteProduit v WHERE v.date >= :dateDebut AND v.date <= :dateFin ORDER BY v.date DESC")
    List<VenteProduit> findVentesBetweenDates(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}

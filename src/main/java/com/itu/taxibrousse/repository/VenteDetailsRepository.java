package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VenteDetails;
import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenteDetailsRepository extends JpaRepository<VenteDetails, Integer> {
    
    List<VenteDetails> findByVenteProduit(VenteProduit venteProduit);
    
    List<VenteDetails> findByVenteProduit_IdVenteProduit(Integer idVenteProduit);
    
    List<VenteDetails> findByProduit(Produit produit);
    
    List<VenteDetails> findByProduit_IdProduit(Integer idProduit);
    
    @Query("SELECT vd FROM VenteDetails vd WHERE vd.date BETWEEN :dateDebut AND :dateFin")
    List<VenteDetails> findByDateBetween(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}

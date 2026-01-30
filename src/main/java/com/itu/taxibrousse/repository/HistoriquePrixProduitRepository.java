package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.HistoriquePrixProduit;
import com.itu.taxibrousse.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoriquePrixProduitRepository extends JpaRepository<HistoriquePrixProduit, Integer> {
    
    List<HistoriquePrixProduit> findByProduitOrderByDateDesc(Produit produit);
    
    List<HistoriquePrixProduit> findByProduit_IdProduitOrderByDateDesc(Integer idProduit);
    
    @Query("SELECT h FROM HistoriquePrixProduit h WHERE h.produit.idProduit = :idProduit AND h.date <= :date ORDER BY h.date DESC")
    List<HistoriquePrixProduit> findByProduitAndDateBeforeOrEqual(@Param("idProduit") Integer idProduit, @Param("date") LocalDate date);
    
    Optional<HistoriquePrixProduit> findFirstByProduit_IdProduitOrderByDateDesc(Integer idProduit);
}

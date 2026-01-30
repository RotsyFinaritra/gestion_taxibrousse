package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    
    Optional<Produit> findByNom(String nom);
    
    List<Produit> findByNomContainingIgnoreCase(String nom);
}

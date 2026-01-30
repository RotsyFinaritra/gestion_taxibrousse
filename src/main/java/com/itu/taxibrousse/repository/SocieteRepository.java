package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocieteRepository extends JpaRepository<Societe, Integer> {
    
    Optional<Societe> findByNom(String nom);
    
    List<Societe> findByNomContainingIgnoreCase(String nom);
    
    @Query("SELECT s FROM Societe s WHERE s.email = :email")
    Optional<Societe> findByEmail(@Param("email") String email);
    
    @Query("SELECT s FROM Societe s ORDER BY s.nom ASC")
    List<Societe> findAllOrderByNom();
}
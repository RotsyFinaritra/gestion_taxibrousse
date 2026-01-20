package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {
    Optional<Ville> findByNom(String nom);
    boolean existsByNom(String nom);
}

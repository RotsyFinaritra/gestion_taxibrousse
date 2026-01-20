package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VoyageVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoyageVehiculeRepository extends JpaRepository<VoyageVehicule, Integer> {
    List<VoyageVehicule> findByVoyage_IdVoyage(Integer idVoyage);
    List<VoyageVehicule> findByVehicule_IdVehicule(Integer idVehicule);
}

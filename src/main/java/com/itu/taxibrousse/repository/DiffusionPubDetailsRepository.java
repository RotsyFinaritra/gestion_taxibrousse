package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.DiffusionPubDetails;
import com.itu.taxibrousse.entity.VoyageVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiffusionPubDetailsRepository extends JpaRepository<DiffusionPubDetails, Integer> {
    
    List<DiffusionPubDetails> findByVoyageVehicule(VoyageVehicule voyageVehicule);
    
    List<DiffusionPubDetails> findByVoyageVehicule_IdVoyageVehicule(Integer idVoyageVehicule);
    
    List<DiffusionPubDetails> findByDiffusionPublicitaire_IdDiffusionPublicitaire(Integer idDiffusionPublicitaire);
    
    @Query("SELECT d FROM DiffusionPubDetails d WHERE d.voyageVehicule.idVoyageVehicule = :voyageVehiculeId")
    List<DiffusionPubDetails> findByVoyageVehiculeId(@Param("voyageVehiculeId") Integer voyageVehiculeId);
    
    @Query("SELECT d FROM DiffusionPubDetails d WHERE EXTRACT(MONTH FROM d.dateDiffusion) = :mois AND EXTRACT(YEAR FROM d.dateDiffusion) = :annee")
    List<DiffusionPubDetails> findByMonthAndYear(@Param("mois") Integer mois, @Param("annee") Integer annee);
}

package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.PaiementDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementDetailsRepository extends JpaRepository<PaiementDetails, Integer> {
    List<PaiementDetails> findByPaiementDiffusion_IdPaiementDiffusion(Integer idPaiementDiffusion);
    List<PaiementDetails> findByDiffusionDetails_IdDiffusionPubDetails(Integer idDiffusionPubDetails);
}
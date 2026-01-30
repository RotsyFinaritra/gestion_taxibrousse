package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.PaiementDiffusion;
import com.itu.taxibrousse.entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaiementDiffusionRepository extends JpaRepository<PaiementDiffusion, Integer> {
    List<PaiementDiffusion> findByDiffusionPublicitaire_Societe(Societe societe);
    List<PaiementDiffusion> findByMoisAnneeAPayerBetween(LocalDate start, LocalDate end);

    Page<PaiementDiffusion> findAll(Pageable pageable);
    Page<PaiementDiffusion> findByDiffusionPublicitaire_Societe(Societe societe, Pageable pageable);
    Page<PaiementDiffusion> findByMoisAnneeAPayerBetween(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM PaiementDiffusion p WHERE p.diffusionPublicitaire.societe.idSociete = :societeId AND p.moisAnneeAPayer BETWEEN :start AND :end")
    BigDecimal sumMontantBySocieteAndMoisAnneeBetween(@Param("societeId") Integer societeId,
                                                     @Param("start") LocalDate start,
                                                     @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM PaiementDiffusion p WHERE p.moisAnneeAPayer BETWEEN :start AND :end")
    BigDecimal sumMontantByMoisAnneeBetween(@Param("start") LocalDate start,
                                           @Param("end") LocalDate end);
}

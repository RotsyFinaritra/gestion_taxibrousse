package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.PaiementDiffusion;
import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.repository.PaiementDiffusionRepository;
import com.itu.taxibrousse.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.math.RoundingMode;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;import org.springframework.data.domain.PageImpl;import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PaiementDiffusionService {

    @Autowired
    private PaiementDiffusionRepository paiementDiffusionRepository;

    @Autowired
    private SocieteRepository societeRepository;

    @Autowired
    private DiffusionPublicitaireService diffusionPublicitaireService;

    @Autowired
    private DiffusionPubDetailsService diffusionPubDetailsService;

    public List<PaiementDiffusion> findAll() {
        return paiementDiffusionRepository.findAll();
    }

    public Optional<PaiementDiffusion> findById(Integer id) {
        return paiementDiffusionRepository.findById(id);
    }

    public PaiementDiffusion save(PaiementDiffusion paiement) {
        return paiementDiffusionRepository.save(paiement);
    }

    public void deleteById(Integer id) {
        paiementDiffusionRepository.deleteById(id);
    }

    public List<PaiementDiffusion> findBySocieteId(Integer idSociete) {
        return societeRepository.findById(idSociete)
                .map(s -> paiementDiffusionRepository.findByDiffusionPublicitaire_Societe(s))
                .orElse(Collections.emptyList());
    }

    public Page<PaiementDiffusion> findBySocieteId(Integer idSociete, Pageable pageable) {
        return societeRepository.findById(idSociete)
                .map(s -> paiementDiffusionRepository.findByDiffusionPublicitaire_Societe(s, pageable))
                .orElse(new PageImpl<>(Collections.emptyList(), pageable, 0));
    }

    public List<PaiementDiffusion> findByDatePaiementBetween(LocalDate dateDebut, LocalDate dateFin) {
        // Version simplifiée pour éviter les erreurs
        return paiementDiffusionRepository.findAll();
    }

    public List<PaiementDiffusion> findByMoisAnnee(Integer mois, Integer annee) {
        // Version simplifiée pour éviter les erreurs
        return paiementDiffusionRepository.findAll();
    }

    public BigDecimal getTotalPaiementsBySociete(Integer idSociete) {
        // Version simplifiée pour éviter les erreurs
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalPaiements() {
        // Version simplifiée pour éviter les erreurs
        return BigDecimal.ZERO;
    }

    // Version simplifiée pour la nouvelle structure
    public BigDecimal calculateMontantDu(int mois, int annee) {
        return BigDecimal.ZERO; // Temporairement retourner 0 pour éviter les erreurs
    }

    // Version simplifiée pour la migration en cours
    public static class RapportPaiementDiffusion {
        private final Societe societe;
        private final BigDecimal montantDu;
        private final BigDecimal montantPaye;
        private final BigDecimal montantRestant;
        private final Long nombreDiffusions;

        public RapportPaiementDiffusion(Societe societe, BigDecimal montantDu, BigDecimal montantPaye, Long nombreDiffusions) {
            this.societe = societe;
            this.montantDu = montantDu != null ? montantDu : BigDecimal.ZERO;
            this.montantPaye = montantPaye != null ? montantPaye : BigDecimal.ZERO;
            this.montantRestant = this.montantDu.subtract(this.montantPaye);
            this.nombreDiffusions = nombreDiffusions != null ? nombreDiffusions : 0L;
        }

        // Getters
        public Societe getSociete() { return societe; }
        public BigDecimal getMontantDu() { return montantDu; }
        public BigDecimal getMontantPaye() { return montantPaye; }
        public BigDecimal getMontantRestant() { return montantRestant; }
        public Long getNombreDiffusions() { return nombreDiffusions; }
        
        // Pourcentage payé (0-100). Retourne Double pour compatibilité avec les comparaisons SpEL
        public Double getPourcentagePaye() {
            try {
                if (montantDu == null || montantDu.compareTo(BigDecimal.ZERO) == 0) {
                    return 0.0;
                }
                BigDecimal pct = montantPaye.multiply(BigDecimal.valueOf(100)).divide(montantDu, 4, RoundingMode.HALF_UP);
                return pct.doubleValue();
            } catch (Exception ex) {
                return 0.0;
            }
        }
    }

    public List<RapportPaiementDiffusion> getRapportPaiements(int mois, int annee) {
        List<Societe> societes = societeRepository.findAll();
        return societes.stream()
            .map(societe -> {
                BigDecimal montantPaye = getTotalPaiementsBySociete(societe.getIdSociete());
                return new RapportPaiementDiffusion(societe, BigDecimal.ZERO, montantPaye, 0L);
            })
            .collect(Collectors.toList());
    }
}
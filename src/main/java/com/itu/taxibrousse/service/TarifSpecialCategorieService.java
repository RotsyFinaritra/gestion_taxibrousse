package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.TarifSpecialCategorie;
import com.itu.taxibrousse.repository.TarifSpecialCategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;
import com.itu.taxibrousse.entity.TarifClasseCategorieTrajet;

import java.math.BigDecimal;

@Service
public class TarifSpecialCategorieService {
    private final TarifSpecialCategorieRepository repo;

    public TarifSpecialCategorieService(TarifSpecialCategorieRepository repo) {
        this.repo = repo;
    }

    public List<TarifSpecialCategorie> findAll() { return repo.findAll(); }
    public Optional<TarifSpecialCategorie> findById(Integer id) { return repo.findById(id); }
    public TarifSpecialCategorie save(TarifSpecialCategorie t) { return repo.save(t); }
    public void deleteById(Integer id) { repo.deleteById(id); }

    public List<TarifSpecialCategorie> findByClasseCategorieAndCategorie(Integer classeCategorieId, Integer categorieId) {
        if (classeCategorieId == null || categorieId == null) return java.util.Collections.emptyList();
        java.util.List<TarifSpecialCategorie> out = new java.util.ArrayList<>();
        for (TarifSpecialCategorie t : repo.findAll()) {
            if (t.getClasseCategorie() == null || t.getCategorie() == null) continue;
            if (t.getClasseCategorie().getIdClasseCategorie() == null || t.getCategorie().getIdCategorie() == null) continue;
            Integer tCcId = t.getClasseCategorie().getIdClasseCategorie();
            Integer tCatId = t.getCategorie().getIdCategorie();
            if (Objects.equals(tCcId, classeCategorieId) && Objects.equals(tCatId, categorieId)) {
                out.add(t);
            }
        }
        return out;
    }

    @Transactional(readOnly = true)
    public List<TarifSpecialCategorie> findByCategorie(Integer categorieId) {
        if (categorieId == null) return java.util.Collections.emptyList();
        java.util.List<TarifSpecialCategorie> out = new java.util.ArrayList<>();
        for (TarifSpecialCategorie t : repo.findAll()) {
            if (t.getCategorie() == null || t.getCategorie().getIdCategorie() == null) continue;
            Integer tCatId = t.getCategorie().getIdCategorie();
            if (Objects.equals(tCatId, categorieId)) {
                out.add(t);
            }
        }
        return out;
    }

    @Transactional(readOnly = true)
    public List<TarifSpecialCategorie> findByClasseAndCategorie(Integer classeId, Integer categorieId) {
        if (classeId == null || categorieId == null) return java.util.Collections.emptyList();
        java.util.List<TarifSpecialCategorie> out = new java.util.ArrayList<>();
        for (TarifSpecialCategorie t : repo.findAll()) {
            if (t.getClasseCategorie() == null || t.getClasseCategorie().getClasse() == null || t.getCategorie() == null) continue;
            if (t.getClasseCategorie().getClasse().getIdClasse() == null || t.getCategorie().getIdCategorie() == null) continue;
            Integer tClasseId = t.getClasseCategorie().getClasse().getIdClasse();
            Integer tCatId = t.getCategorie().getIdCategorie();
            if (Objects.equals(tClasseId, classeId) && Objects.equals(tCatId, categorieId)) {
                out.add(t);
            }
        }
        return out;
    }

    /**
     * Backward-compatible alias: return TarifSpecialCategorie entries matching
     * the given classe id (classeCategorie.classe.idClasse) and categorie id.
     */
    @Transactional(readOnly = true)
    public List<TarifSpecialCategorie> getByClasseAndCategorie(Integer classeId, Integer categorieId) {
        return findByClasseAndCategorie(classeId, categorieId);
    }

    public java.math.BigDecimal applyRemise(java.math.BigDecimal base, TarifSpecialCategorie tsc) {
        if (base == null || tsc == null || tsc.getRemise() == null) return null;
        System.out.println("===== Applying remise calculation =====");
        System.out.println("Base price: " + base);
        java.math.BigDecimal remise = tsc.getRemise();
        System.out.println("Remise percentage: " + remise);
        java.math.BigDecimal discount = base.multiply(remise).divide(java.math.BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        return base.subtract(discount).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Find the appropriate base montant among the provided tccList for the given
     * TarifSpecialCategorie and apply the remise. If no dedicated base is found,
     * fallbackMontant is used (may be null). Returns discounted price or null.
     */
    public BigDecimal computeSpecialPrice(TarifSpecialCategorie tsc, List<TarifClasseCategorieTrajet> tccList, BigDecimal fallbackMontant) {
        if (tsc == null) return null;
        try {
            if (tsc.getClasseCategorie() != null && tsc.getClasseCategorie().getIdClasseCategorie() != null && tccList != null) {
                Integer specialCcId = tsc.getClasseCategorie().getIdClasseCategorie();
                for (TarifClasseCategorieTrajet baseCandidate : tccList) {
                    if (baseCandidate != null && baseCandidate.getClasseCategorie() != null && baseCandidate.getClasseCategorie().getIdClasseCategorie() != null
                            && baseCandidate.getClasseCategorie().getIdClasseCategorie().equals(specialCcId)
                            && baseCandidate.getMontant() != null && baseCandidate.getClasseCategorie().getCategorie().getIdCategorie() != tsc.getCategorie().getIdCategorie()) {
                        return applyRemise(baseCandidate.getMontant(), tsc);
                    }
                }
            }
        } catch (Exception ignore) {}
        if (fallbackMontant != null) {
            return applyRemise(fallbackMontant, tsc);
        }
        return null;
    }
}

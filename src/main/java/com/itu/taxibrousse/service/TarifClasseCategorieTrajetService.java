package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.TarifClasseCategorieTrajet;
import com.itu.taxibrousse.repository.TarifClasseCategorieTrajetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class TarifClasseCategorieTrajetService {
    private final TarifClasseCategorieTrajetRepository repo;

    public TarifClasseCategorieTrajetService(TarifClasseCategorieTrajetRepository repo) {
        this.repo = repo;
    }

    public List<TarifClasseCategorieTrajet> findAll() { return repo.findAll(); }
    public Optional<TarifClasseCategorieTrajet> findById(Integer id) { return repo.findById(id); }
    public void deleteById(Integer id) { repo.deleteById(id); }

    public List<TarifClasseCategorieTrajet> findByTrajetIdAndDate(Integer trajetId, LocalDate date) {
        if (trajetId == null) return java.util.Collections.emptyList();
        java.util.List<TarifClasseCategorieTrajet> out = new java.util.ArrayList<>();
        for (TarifClasseCategorieTrajet t : repo.findAll()) {
            if (t.getTrajet() == null || t.getTrajet().getIdTrajet() == null) continue;
            if (!t.getTrajet().getIdTrajet().equals(trajetId)) continue;
            out.add(t);
        }
        return out;
    }

    public TarifClasseCategorieTrajet save(TarifClasseCategorieTrajet t) {
        if (t == null) return null;
        // validation: prevent duplication by classeCategorie + trajet
        try {
            Integer newId = t.getIdTarifClasseCategorieTrajet();
            Integer ccId = t.getClasseCategorie() != null ? t.getClasseCategorie().getIdClasseCategorie() : null;
            Integer trajetId = t.getTrajet() != null ? t.getTrajet().getIdTrajet() : null;
            if (ccId != null && trajetId != null) {
                for (TarifClasseCategorieTrajet existing : repo.findAll()) {
                    Integer exCc = existing.getClasseCategorie() != null ? existing.getClasseCategorie().getIdClasseCategorie() : null;
                    Integer exTrajet = existing.getTrajet() != null ? existing.getTrajet().getIdTrajet() : null;
                    if (exCc != null && exTrajet != null && exCc.equals(ccId) && exTrajet.equals(trajetId)) {
                        if (newId == null || !newId.equals(existing.getIdTarifClasseCategorieTrajet())) {
                            throw new IllegalArgumentException("Un tarif pour cette combinaison classe/catégorie et trajet existe déjà.");
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            throw e;
        }
        return repo.save(t);
    }
}

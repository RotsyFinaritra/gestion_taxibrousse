package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.TarifClasseTrajet;
import com.itu.taxibrousse.repository.TarifClasseTrajetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class TarifClasseTrajetService {
    private final TarifClasseTrajetRepository repo;

    public TarifClasseTrajetService(TarifClasseTrajetRepository repo) {
        this.repo = repo;
    }

    public List<TarifClasseTrajet> findAll() { return repo.findAll(); }
    public Optional<TarifClasseTrajet> findById(Integer id) { return repo.findById(id); }
    public TarifClasseTrajet save(TarifClasseTrajet t) { return repo.save(t); }
    public void deleteById(Integer id) { repo.deleteById(id); }

    public List<TarifClasseTrajet> findByTrajetIdAndDate(Integer trajetId, LocalDate date) {
        if (trajetId == null) return java.util.Collections.emptyList();
        java.util.List<TarifClasseTrajet> out = new java.util.ArrayList<>();
        for (TarifClasseTrajet t : repo.findAll()) {
            if (t.getTrajet() == null || t.getTrajet().getIdTrajet() == null) continue;
            if (!t.getTrajet().getIdTrajet().equals(trajetId)) continue;
            if (!((t.getDate() == null && date == null) || (t.getDate() != null && t.getDate().equals(date)))) continue;
            out.add(t);
        }
        return out;
    }
}

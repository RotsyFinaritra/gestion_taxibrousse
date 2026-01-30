package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.repository.SocieteRepository;
import com.itu.taxibrousse.service.HistoriqueTarifDiffusionService;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
@Transactional
public class SocieteService {

    private final SocieteRepository societeRepository;
    private final HistoriqueTarifDiffusionService historiqueService;

    public SocieteService(SocieteRepository societeRepository, HistoriqueTarifDiffusionService historiqueService) {
        this.societeRepository = societeRepository;
        this.historiqueService = historiqueService;
    }

    public List<Societe> findAll() {
        return societeRepository.findAll();
    }

    public List<Societe> findAllOrderByNom() {
        return societeRepository.findAllOrderByNom();
    }

    public Optional<Societe> findById(Integer id) {
        return societeRepository.findById(id);
    }

    public Optional<Societe> findByNom(String nom) {
        return societeRepository.findByNom(nom);
    }

    public Optional<Societe> findByEmail(String email) {
        return societeRepository.findByEmail(email);
    }

    public List<Societe> findByNomContaining(String nom) {
        return societeRepository.findByNomContainingIgnoreCase(nom);
    }

    public Societe save(Societe societe) {
        // Used for creation via controller
        boolean isNew = (societe.getIdSociete() == null);
        Societe saved = societeRepository.save(societe);

        if (isNew && saved.getTarifParDiffusion() != null) {
            try {
                historiqueService.creerHistorique(saved.getIdSociete(), saved.getTarifParDiffusion(), LocalDate.now());
            } catch (IllegalArgumentException ignored) {
                // ignore duplicate historique for same date
            }
        }

        return saved;
    }

    public Societe create(Societe societe) {
        return save(societe);
    }

    public Societe update(Societe societe) {
        if (societe.getIdSociete() == null) {
            throw new IllegalArgumentException("L'ID de la société ne peut pas être null pour une mise à jour");
        }

        // Retrieve existing to compare tarif
        Optional<Societe> existingOpt = societeRepository.findById(societe.getIdSociete());
        BigDecimal oldTarif = null;
        if (existingOpt.isPresent()) {
            oldTarif = existingOpt.get().getTarifParDiffusion();
        }

        Societe saved = societeRepository.save(societe);

        BigDecimal newTarif = saved.getTarifParDiffusion();
        boolean tarifChanged = false;
        if (oldTarif == null && newTarif != null) {
            tarifChanged = true;
        } else if (oldTarif != null && newTarif == null) {
            tarifChanged = true;
        } else if (oldTarif != null && newTarif != null && oldTarif.compareTo(newTarif) != 0) {
            tarifChanged = true;
        }

        if (tarifChanged) {
            try {
                historiqueService.creerHistorique(saved.getIdSociete(), newTarif != null ? newTarif : BigDecimal.ZERO, LocalDate.now());
            } catch (IllegalArgumentException ignored) {
                // ignore if historique for today already exists
            }
        }

        return saved;
    }

    public void deleteById(Integer id) {
        societeRepository.deleteById(id);
    }

    public void delete(Societe societe) {
        societeRepository.delete(societe);
    }

    public boolean existsById(Integer id) {
        return societeRepository.existsById(id);
    }

    public long count() {
        return societeRepository.count();
    }
}
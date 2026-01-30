package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.DiffusionPublicitaire;
import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.repository.DiffusionPublicitaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiffusionPublicitaireService {
    
    private final DiffusionPublicitaireRepository diffusionPublicitaireRepository;
    private final SocieteService societeService;
    
    public DiffusionPublicitaireService(DiffusionPublicitaireRepository diffusionPublicitaireRepository,
                                        SocieteService societeService) {
        this.diffusionPublicitaireRepository = diffusionPublicitaireRepository;
        this.societeService = societeService;
    }
    
    public List<DiffusionPublicitaire> findAll() {
        return diffusionPublicitaireRepository.findAll();
    }
    
    public Optional<DiffusionPublicitaire> findById(Integer id) {
        return diffusionPublicitaireRepository.findById(id);
    }
    
    public DiffusionPublicitaire save(DiffusionPublicitaire diffusionPublicitaire) {
        return diffusionPublicitaireRepository.save(diffusionPublicitaire);
    }
    
    public void deleteById(Integer id) {
        diffusionPublicitaireRepository.deleteById(id);
    }
    
    public List<DiffusionPublicitaire> findBySocieteId(Integer idSociete) {
        return diffusionPublicitaireRepository.findBySociete_IdSociete(idSociete);
    }
    
    public List<DiffusionPublicitaire> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin) {
        return diffusionPublicitaireRepository.findByDateDebutBetween(dateDebut, dateFin);
    }
    
    public List<DiffusionPublicitaire> findActivesAujourdhui() {
        return diffusionPublicitaireRepository.findActivesADate(LocalDate.now());
    }
    
    public List<DiffusionPublicitaire> findNonCloturees() {
        return diffusionPublicitaireRepository.findByDateCloturageIsNull();
    }
    
    public void cloturerDiffusion(Integer id, LocalDate dateCloturage) {
        Optional<DiffusionPublicitaire> diffusionOpt = findById(id);
        if (diffusionOpt.isPresent()) {
            DiffusionPublicitaire diffusion = diffusionOpt.get();
            diffusion.setDateCloturage(dateCloturage);
            save(diffusion);
        }
    }
}
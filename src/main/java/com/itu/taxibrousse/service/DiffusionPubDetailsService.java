package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.DiffusionPubDetails;
import com.itu.taxibrousse.repository.DiffusionPubDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DiffusionPubDetailsService {
    
    private final DiffusionPubDetailsRepository diffusionPubDetailsRepository;
    
    public DiffusionPubDetailsService(DiffusionPubDetailsRepository diffusionPubDetailsRepository) {
        this.diffusionPubDetailsRepository = diffusionPubDetailsRepository;
    }
    
    public List<DiffusionPubDetails> findAll() {
        return diffusionPubDetailsRepository.findAll();
    }
    
    public Optional<DiffusionPubDetails> findById(Integer id) {
        return diffusionPubDetailsRepository.findById(id);
    }
    
    public DiffusionPubDetails save(DiffusionPubDetails diffusionPubDetails) {
        return diffusionPubDetailsRepository.save(diffusionPubDetails);
    }
    
    public void deleteById(Integer id) {
        diffusionPubDetailsRepository.deleteById(id);
    }
    
    public List<DiffusionPubDetails> findByVoyageVehiculeId(Integer idVoyageVehicule) {
        return diffusionPubDetailsRepository.findByVoyageVehicule_IdVoyageVehicule(idVoyageVehicule);
    }
    
    public List<DiffusionPubDetails> findByDiffusionPublicitaireId(Integer idDiffusionPublicitaire) {
        return diffusionPubDetailsRepository.findByDiffusionPublicitaire_IdDiffusionPublicitaire(idDiffusionPublicitaire);
    }
    
    public BigDecimal calculateRevenue(Integer voyageVehiculeId) {
        List<DiffusionPubDetails> details = findByVoyageVehiculeId(voyageVehiculeId);
        return details.stream()
            .filter(d -> d.getVideoPublicitaire() != null && d.getVideoPublicitaire().getSociete() != null)
            .map(d -> {
                BigDecimal tarif = d.getVideoPublicitaire().getSociete().getTarifParDiffusion();
                Integer nbDiffusions = d.getNbDiffusion();
                return tarif.multiply(BigDecimal.valueOf(nbDiffusions != null ? nbDiffusions : 0));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calculateRevenueByMonth(Integer mois, Integer annee) {
        List<DiffusionPubDetails> details = diffusionPubDetailsRepository.findByMonthAndYear(mois, annee);
        return details.stream()
            .filter(d -> d.getVideoPublicitaire() != null && d.getVideoPublicitaire().getSociete() != null)
            .map(d -> {
                BigDecimal tarif = d.getVideoPublicitaire().getSociete().getTarifParDiffusion();
                Integer nbDiffusions = d.getNbDiffusion();
                return tarif.multiply(BigDecimal.valueOf(nbDiffusions != null ? nbDiffusions : 0));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getTotalRevenue() {
        List<DiffusionPubDetails> details = findAll();
        return details.stream()
            .filter(d -> d.getVideoPublicitaire() != null && d.getVideoPublicitaire().getSociete() != null)
            .map(d -> {
                BigDecimal tarif = d.getVideoPublicitaire().getSociete().getTarifParDiffusion();
                Integer nbDiffusions = d.getNbDiffusion();
                return tarif.multiply(BigDecimal.valueOf(nbDiffusions != null ? nbDiffusions : 0));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

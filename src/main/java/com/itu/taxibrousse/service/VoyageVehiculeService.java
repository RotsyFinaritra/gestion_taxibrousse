package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.VoyageVehicule;
import com.itu.taxibrousse.repository.VoyageVehiculeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VoyageVehiculeService {
    
    private final VoyageVehiculeRepository voyageVehiculeRepository;
    
    public VoyageVehiculeService(VoyageVehiculeRepository voyageVehiculeRepository) {
        this.voyageVehiculeRepository = voyageVehiculeRepository;
    }
    
    public List<VoyageVehicule> findAll() {
        return voyageVehiculeRepository.findAll();
    }
    
    public Optional<VoyageVehicule> findById(Integer id) {
        return voyageVehiculeRepository.findById(id);
    }
    
    public VoyageVehicule save(VoyageVehicule voyageVehicule) {
        return voyageVehiculeRepository.save(voyageVehicule);
    }
    
    public void deleteById(Integer id) {
        voyageVehiculeRepository.deleteById(id);
    }
    
    public List<VoyageVehicule> findByVoyage(Integer idVoyage) {
        return voyageVehiculeRepository.findByVoyage_IdVoyage(idVoyage);
    }
    
    public List<VoyageVehicule> findByVehicule(Integer idVehicule) {
        return voyageVehiculeRepository.findByVehicule_IdVehicule(idVehicule);
    }

    public List<VoyageVehicule> findWithFilters(Integer id, Integer voyageId, String dateDepartStr,
                                                Integer vehiculeId, String immatriculation, String prixOrder) {
        java.time.LocalDate dateDepart = null;
        try {
            if (dateDepartStr != null && !dateDepartStr.isEmpty()) {
                dateDepart = java.time.LocalDate.parse(dateDepartStr);
            }
        } catch (Exception e) {
            // ignore parse errors and treat as no filter
        }
        return voyageVehiculeRepository.findWithFilters(id, voyageId, dateDepart, vehiculeId, immatriculation, prixOrder);
    }
}

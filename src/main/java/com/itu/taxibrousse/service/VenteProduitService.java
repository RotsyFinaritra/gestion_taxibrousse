package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.entity.VoyageVehicule;
import com.itu.taxibrousse.repository.VenteProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VenteProduitService {
    
    private final VenteProduitRepository venteProduitRepository;
    
    public VenteProduitService(VenteProduitRepository venteProduitRepository) {
        this.venteProduitRepository = venteProduitRepository;
    }
    
    public List<VenteProduit> findAll() {
        return venteProduitRepository.findAll();
    }
    
    public Optional<VenteProduit> findById(Integer id) {
        return venteProduitRepository.findById(id);
    }
    
    public List<VenteProduit> findByVoyageVehicule(VoyageVehicule voyageVehicule) {
        return venteProduitRepository.findByVoyageVehiculeOrderByDateDesc(voyageVehicule);
    }
    
    public List<VenteProduit> findByVoyageVehiculeId(Integer idVoyageVehicule) {
        return venteProduitRepository.findByVoyageVehicule_IdVoyageVehiculeOrderByDateDesc(idVoyageVehicule);
    }
    
    public List<VenteProduit> findByDateBetween(LocalDate dateDebut, LocalDate dateFin) {
        return venteProduitRepository.findVentesBetweenDates(dateDebut, dateFin);
    }
    
    public VenteProduit save(VenteProduit venteProduit) {
        return venteProduitRepository.save(venteProduit);
    }
    
    public void deleteById(Integer id) {
        venteProduitRepository.deleteById(id);
    }
    
    public boolean existsById(Integer id) {
        return venteProduitRepository.existsById(id);
    }
    
    public long count() {
        return venteProduitRepository.count();
    }
}

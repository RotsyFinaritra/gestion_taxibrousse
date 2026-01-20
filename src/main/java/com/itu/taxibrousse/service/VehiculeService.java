package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Vehicule;
import com.itu.taxibrousse.repository.VehiculeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VehiculeService {
    
    private final VehiculeRepository vehiculeRepository;
    
    public VehiculeService(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }
    
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }
    
    public Optional<Vehicule> findById(Integer id) {
        return vehiculeRepository.findById(id);
    }
    
    public Vehicule save(Vehicule vehicule) {
        return vehiculeRepository.save(vehicule);
    }
    
    public void deleteById(Integer id) {
        vehiculeRepository.deleteById(id);
    }
    
    public Optional<Vehicule> findByImmatriculation(String immatriculation) {
        return vehiculeRepository.findByImmatriculation(immatriculation);
    }
    
    public boolean existsByImmatriculation(String immatriculation) {
        return vehiculeRepository.existsByImmatriculation(immatriculation);
    }
}

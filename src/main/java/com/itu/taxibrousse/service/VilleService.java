package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Ville;
import com.itu.taxibrousse.repository.VilleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VilleService {
    
    private final VilleRepository villeRepository;
    
    public VilleService(VilleRepository villeRepository) {
        this.villeRepository = villeRepository;
    }
    
    public List<Ville> findAll() {
        return villeRepository.findAll();
    }
    
    public Optional<Ville> findById(Integer id) {
        return villeRepository.findById(id);
    }
    
    public Ville save(Ville ville) {
        return villeRepository.save(ville);
    }
    
    public void deleteById(Integer id) {
        villeRepository.deleteById(id);
    }
    
    public boolean existsByNom(String nom) {
        return villeRepository.existsByNom(nom);
    }
}

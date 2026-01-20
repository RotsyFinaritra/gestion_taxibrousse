package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.HeureDepart;
import com.itu.taxibrousse.repository.HeureDepartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HeureDepartService {
    
    private final HeureDepartRepository heureDepartRepository;
    
    public HeureDepartService(HeureDepartRepository heureDepartRepository) {
        this.heureDepartRepository = heureDepartRepository;
    }
    
    public List<HeureDepart> findAll() {
        return heureDepartRepository.findAllByOrderByHeureAsc();
    }
    
    public Optional<HeureDepart> findById(Integer id) {
        return heureDepartRepository.findById(id);
    }
    
    public HeureDepart save(HeureDepart heureDepart) {
        return heureDepartRepository.save(heureDepart);
    }
    
    public void deleteById(Integer id) {
        heureDepartRepository.deleteById(id);
    }
    
    public List<HeureDepart> findByHeure(LocalTime heure) {
        return heureDepartRepository.findByHeureOrderByHeure(heure);
    }
}

package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Voyage;
import com.itu.taxibrousse.repository.VoyageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VoyageService {
    
    private final VoyageRepository voyageRepository;
    
    public VoyageService(VoyageRepository voyageRepository) {
        this.voyageRepository = voyageRepository;
    }
    
    public List<Voyage> findAll() {
        return voyageRepository.findAll();
    }
    
    public Optional<Voyage> findById(Integer id) {
        return voyageRepository.findById(id);
    }
    
    public Voyage save(Voyage voyage) {
        return voyageRepository.save(voyage);
    }
    
    public void deleteById(Integer id) {
        voyageRepository.deleteById(id);
    }
    
    public List<Voyage> findByDateDepart(LocalDate dateDepart) {
        return voyageRepository.findByDateDepart(dateDepart);
    }
    
    public List<Voyage> findByTrajet(Integer idTrajet) {
        return voyageRepository.findByTrajet_IdTrajet(idTrajet);
    }
    
    public List<Voyage> findUpcomingVoyages() {
        return voyageRepository.findByDateDepartAfterOrderByDateDepartAsc(LocalDate.now());
    }
}

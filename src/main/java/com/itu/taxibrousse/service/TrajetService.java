package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Trajet;
import com.itu.taxibrousse.repository.TrajetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrajetService {
    
    private final TrajetRepository trajetRepository;
    
    public TrajetService(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }
    
    public List<Trajet> findAll() {
        return trajetRepository.findAll();
    }
    
    public Optional<Trajet> findById(Integer id) {
        return trajetRepository.findById(id);
    }
    
    public Trajet save(Trajet trajet) {
        return trajetRepository.save(trajet);
    }
    
    public void deleteById(Integer id) {
        trajetRepository.deleteById(id);
    }
    
    public List<Trajet> findByGareRoutiereDepart(Integer idGareRoutiereDepart) {
        return trajetRepository.findByGareRoutiereDepart_IdGareRoutiere(idGareRoutiereDepart);
    }
    
    public List<Trajet> findByGareRoutiereArrivee(Integer idGareRoutiereArrivee) {
        return trajetRepository.findByGareRoutiereArrivee_IdGareRoutiere(idGareRoutiereArrivee);
    }
    
    public List<Trajet> findByVillesDepartEtArrivee(Integer idVilleDepart, Integer idVilleArrivee) {
        return trajetRepository.findByGareRoutiereDepart_Ville_IdVilleAndGareRoutiereArrivee_Ville_IdVille(idVilleDepart, idVilleArrivee);
    }
}

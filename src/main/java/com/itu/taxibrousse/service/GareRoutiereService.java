package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.GareRoutiere;
import com.itu.taxibrousse.repository.GareRoutiereRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GareRoutiereService {
    
    private final GareRoutiereRepository gareRoutiereRepository;
    
    public GareRoutiereService(GareRoutiereRepository gareRoutiereRepository) {
        this.gareRoutiereRepository = gareRoutiereRepository;
    }
    
    public List<GareRoutiere> findAll() {
        return gareRoutiereRepository.findAll();
    }
    
    public Optional<GareRoutiere> findById(Integer id) {
        return gareRoutiereRepository.findById(id);
    }
    
    public GareRoutiere save(GareRoutiere gareRoutiere) {
        return gareRoutiereRepository.save(gareRoutiere);
    }
    
    public void deleteById(Integer id) {
        gareRoutiereRepository.deleteById(id);
    }
    
    public List<GareRoutiere> findByVille(Integer idVille) {
        return gareRoutiereRepository.findByVille_IdVille(idVille);
    }
}

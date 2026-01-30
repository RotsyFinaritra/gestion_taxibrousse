package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.VenteDetails;
import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.repository.VenteDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VenteDetailsService {
    
    private final VenteDetailsRepository venteDetailsRepository;
    
    public VenteDetailsService(VenteDetailsRepository venteDetailsRepository) {
        this.venteDetailsRepository = venteDetailsRepository;
    }
    
    public List<VenteDetails> findAll() {
        return venteDetailsRepository.findAll();
    }
    
    public Optional<VenteDetails> findById(Integer id) {
        return venteDetailsRepository.findById(id);
    }
    
    public List<VenteDetails> findByVenteProduit(VenteProduit venteProduit) {
        return venteDetailsRepository.findByVenteProduit(venteProduit);
    }
    
    public List<VenteDetails> findByVenteProduitId(Integer idVenteProduit) {
        return venteDetailsRepository.findByVenteProduit_IdVenteProduit(idVenteProduit);
    }
    
    public List<VenteDetails> findByProduit(Produit produit) {
        return venteDetailsRepository.findByProduit(produit);
    }
    
    public List<VenteDetails> findByProduitId(Integer idProduit) {
        return venteDetailsRepository.findByProduit_IdProduit(idProduit);
    }
    
    public List<VenteDetails> findByDateBetween(LocalDate dateDebut, LocalDate dateFin) {
        return venteDetailsRepository.findByDateBetween(dateDebut, dateFin);
    }
    
    public VenteDetails save(VenteDetails venteDetails) {
        return venteDetailsRepository.save(venteDetails);
    }
    
    public void deleteById(Integer id) {
        venteDetailsRepository.deleteById(id);
    }
    
    public boolean existsById(Integer id) {
        return venteDetailsRepository.existsById(id);
    }
    
    public long count() {
        return venteDetailsRepository.count();
    }
}

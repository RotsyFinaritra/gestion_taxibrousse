package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProduitService {
    
    private final ProduitRepository produitRepository;
    
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }
    
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }
    
    public Optional<Produit> findById(Integer id) {
        return produitRepository.findById(id);
    }
    
    public Optional<Produit> findByNom(String nom) {
        return produitRepository.findByNom(nom);
    }
    
    public List<Produit> searchByNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }
    
    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }
    
    public void deleteById(Integer id) {
        produitRepository.deleteById(id);
    }
    
    public boolean existsById(Integer id) {
        return produitRepository.existsById(id);
    }
    
    public long count() {
        return produitRepository.count();
    }
}

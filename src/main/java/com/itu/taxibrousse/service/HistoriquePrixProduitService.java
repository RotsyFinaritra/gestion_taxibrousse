package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.HistoriquePrixProduit;
import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.repository.HistoriquePrixProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistoriquePrixProduitService {
    
    private final HistoriquePrixProduitRepository historiquePrixProduitRepository;
    
    public HistoriquePrixProduitService(HistoriquePrixProduitRepository historiquePrixProduitRepository) {
        this.historiquePrixProduitRepository = historiquePrixProduitRepository;
    }
    
    public List<HistoriquePrixProduit> findAll() {
        return historiquePrixProduitRepository.findAll();
    }
    
    public Optional<HistoriquePrixProduit> findById(Integer id) {
        return historiquePrixProduitRepository.findById(id);
    }
    
    public List<HistoriquePrixProduit> findByProduit(Produit produit) {
        return historiquePrixProduitRepository.findByProduitOrderByDateDesc(produit);
    }
    
    public List<HistoriquePrixProduit> findByProduitId(Integer idProduit) {
        return historiquePrixProduitRepository.findByProduit_IdProduitOrderByDateDesc(idProduit);
    }
    
    public List<HistoriquePrixProduit> findByProduitAndDateBeforeOrEqual(Integer idProduit, LocalDate date) {
        return historiquePrixProduitRepository.findByProduitAndDateBeforeOrEqual(idProduit, date);
    }
    
    public Optional<HistoriquePrixProduit> findLatestByProduitId(Integer idProduit) {
        return historiquePrixProduitRepository.findFirstByProduit_IdProduitOrderByDateDesc(idProduit);
    }
    
    /**
     * Get the price of a product at a specific date.
     * Returns the most recent price on or before the given date.
     */
    public Optional<BigDecimal> getPrixAtDate(Integer idProduit, LocalDate date) {
        List<HistoriquePrixProduit> historique = historiquePrixProduitRepository
                .findByProduitAndDateBeforeOrEqual(idProduit, date);
        if (!historique.isEmpty()) {
            return Optional.ofNullable(historique.get(0).getMontant());
        }
        return Optional.empty();
    }
    
    public HistoriquePrixProduit save(HistoriquePrixProduit historiquePrixProduit) {
        return historiquePrixProduitRepository.save(historiquePrixProduit);
    }
    
    public void deleteById(Integer id) {
        historiquePrixProduitRepository.deleteById(id);
    }
    
    public long count() {
        return historiquePrixProduitRepository.count();
    }
}

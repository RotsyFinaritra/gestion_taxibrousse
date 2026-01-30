package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.HistoriqueTarifDiffusion;
import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.repository.HistoriqueTarifDiffusionRepository;
import com.itu.taxibrousse.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueTarifDiffusionService {
    
    @Autowired
    private HistoriqueTarifDiffusionRepository historiqueTarifDiffusionRepository;

    @Autowired
    private SocieteRepository societeRepository;
    
    /**
     * Récupère tous les historiques de tarifs
     */
    public List<HistoriqueTarifDiffusion> findAll() {
        return historiqueTarifDiffusionRepository.findAll();
    }
    
    /**
     * Trouve un historique par son ID
     */
    public Optional<HistoriqueTarifDiffusion> findById(Integer id) {
        return historiqueTarifDiffusionRepository.findById(id);
    }
    
    /**
     * Sauvegarde un historique de tarif
     */
    public HistoriqueTarifDiffusion save(HistoriqueTarifDiffusion historique) {
        return historiqueTarifDiffusionRepository.save(historique);
    }
    
    /**
     * Supprime un historique par son ID
     */
    public void deleteById(Integer id) {
        historiqueTarifDiffusionRepository.deleteById(id);
    }
    
    /**
     * Trouve tous les historiques pour une société donnée
     */
    public List<HistoriqueTarifDiffusion> findBySociete(Societe societe) {
        return historiqueTarifDiffusionRepository.findBySocieteOrderByDateDesc(societe);
    }
    
    /**
     * Trouve tous les historiques pour une société par son ID
     */
    public List<HistoriqueTarifDiffusion> findBySocieteId(Integer idSociete) {
        return historiqueTarifDiffusionRepository.findBySocieteIdOrderByDateDesc(idSociete);
    }
    
    /**
     * Obtient le tarif valide pour une société à une date donnée
     * (le tarif le plus récent avant ou à cette date)
     */
    public BigDecimal getTarifValidePourSocieteEtDate(Integer idSociete, LocalDate date) {
        List<HistoriqueTarifDiffusion> historiques = 
            historiqueTarifDiffusionRepository.findTarifValidePourSocieteEtDate(idSociete, date);
        
        if (!historiques.isEmpty()) {
            return historiques.get(0).getTarif(); // Le premier est le plus récent
        }
        
        return BigDecimal.ZERO; // Aucun tarif trouvé
    }
    
    /**
     * Trouve les historiques entre deux dates
     */
    public List<HistoriqueTarifDiffusion> findByDateBetween(LocalDate dateDebut, LocalDate dateFin) {
        return historiqueTarifDiffusionRepository.findByDateBetweenOrderByDateDescSocieteNom(dateDebut, dateFin);
    }
    
    /**
     * Obtient les derniers tarifs pour toutes les sociétés
     */
    public List<HistoriqueTarifDiffusion> getDerniersTarifs() {
        return historiqueTarifDiffusionRepository.findDernierTarifParSociete();
    }
    
    /**
     * Crée un nouvel historique de tarif
     */
    public HistoriqueTarifDiffusion creerHistorique(Integer idSociete, BigDecimal tarif, LocalDate date) {
        Optional<Societe> societeOpt = societeRepository.findById(idSociete);

        if (societeOpt.isEmpty()) {
            throw new IllegalArgumentException("Société non trouvée avec l'ID : " + idSociete);
        }
        
        // Vérifier si un tarif existe déjà pour cette société à cette date
        if (historiqueTarifDiffusionRepository.existsBySocieteIdAndDate(idSociete, date)) {
            throw new IllegalArgumentException("Un tarif existe déjà pour cette société à cette date");
        }
        
        HistoriqueTarifDiffusion historique = new HistoriqueTarifDiffusion();
        historique.setSociete(societeOpt.get());
        historique.setTarif(tarif);
        historique.setDate(date);
        
        return save(historique);
    }
    
    /**
     * Met à jour un historique existant
     */
    public HistoriqueTarifDiffusion updateHistorique(Integer id, BigDecimal tarif, LocalDate date) {
        Optional<HistoriqueTarifDiffusion> historiqueOpt = findById(id);
        
        if (historiqueOpt.isEmpty()) {
            throw new IllegalArgumentException("Historique non trouvé avec l'ID : " + id);
        }
        
        HistoriqueTarifDiffusion historique = historiqueOpt.get();
        
        // Vérifier s'il y a un conflit de date avec un autre enregistrement de la même société
        if (!historique.getDate().equals(date) && 
            historiqueTarifDiffusionRepository.existsBySocieteIdAndDate(historique.getSociete().getIdSociete(), date)) {
            throw new IllegalArgumentException("Un tarif existe déjà pour cette société à cette date");
        }
        
        historique.setTarif(tarif);
        historique.setDate(date);
        
        return save(historique);
    }
    
    /**
     * Classe interne pour les statistiques de tarifs
     */
    public static class StatistiquesTarif {
        private String nomSociete;
        private BigDecimal tarifActuel;
        private LocalDate dateDernierTarif;
        private int nombreHistoriques;
        
        public StatistiquesTarif(String nomSociete, BigDecimal tarifActuel, LocalDate dateDernierTarif, int nombreHistoriques) {
            this.nomSociete = nomSociete;
            this.tarifActuel = tarifActuel;
            this.dateDernierTarif = dateDernierTarif;
            this.nombreHistoriques = nombreHistoriques;
        }
        
        // Getters et Setters
        public String getNomSociete() { return nomSociete; }
        public void setNomSociete(String nomSociete) { this.nomSociete = nomSociete; }
        
        public BigDecimal getTarifActuel() { return tarifActuel; }
        public void setTarifActuel(BigDecimal tarifActuel) { this.tarifActuel = tarifActuel; }
        
        public LocalDate getDateDernierTarif() { return dateDernierTarif; }
        public void setDateDernierTarif(LocalDate dateDernierTarif) { this.dateDernierTarif = dateDernierTarif; }
        
        public int getNombreHistoriques() { return nombreHistoriques; }
        public void setNombreHistoriques(int nombreHistoriques) { this.nombreHistoriques = nombreHistoriques; }
    }
    
    /**
     * Obtient les statistiques des tarifs par société
     */
    public List<StatistiquesTarif> getStatistiquesTarifs() {
        List<HistoriqueTarifDiffusion> derniersTarifs = getDerniersTarifs();
        
        return derniersTarifs.stream()
                .map(historique -> {
                    int nombreHistoriques = findBySociete(historique.getSociete()).size();
                    return new StatistiquesTarif(
                            historique.getSociete().getNom(),
                            historique.getTarif(),
                            historique.getDate(),
                            nombreHistoriques
                    );
                })
                .toList();
    }
}
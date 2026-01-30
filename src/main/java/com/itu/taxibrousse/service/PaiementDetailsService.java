package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.PaiementDetails;
import com.itu.taxibrousse.repository.PaiementDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaiementDetailsService {
    
    private final PaiementDetailsRepository paiementDetailsRepository;
    
    public PaiementDetailsService(PaiementDetailsRepository paiementDetailsRepository) {
        this.paiementDetailsRepository = paiementDetailsRepository;
    }
    
    public List<PaiementDetails> findAll() {
        return paiementDetailsRepository.findAll();
    }
    
    public Optional<PaiementDetails> findById(Integer id) {
        return paiementDetailsRepository.findById(id);
    }
    
    public PaiementDetails save(PaiementDetails paiementDetails) {
        return paiementDetailsRepository.save(paiementDetails);
    }
    
    public void deleteById(Integer id) {
        paiementDetailsRepository.deleteById(id);
    }
    
    public List<PaiementDetails> findByPaiementDiffusionId(Integer idPaiementDiffusion) {
        return paiementDetailsRepository.findByPaiementDiffusion_IdPaiementDiffusion(idPaiementDiffusion);
    }
    
    public List<PaiementDetails> findByDiffusionDetailsId(Integer idDiffusionPubDetails) {
        return paiementDetailsRepository.findByDiffusionDetails_IdDiffusionPubDetails(idDiffusionPubDetails);
    }
}
package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.VideoPublicitaire;
import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.repository.VideoPublicitaireRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VideoPublicitaireService {

    private final VideoPublicitaireRepository videoPublicitaireRepository;

    public VideoPublicitaireService(VideoPublicitaireRepository videoPublicitaireRepository) {
        this.videoPublicitaireRepository = videoPublicitaireRepository;
    }

    public List<VideoPublicitaire> findAll() {
        return videoPublicitaireRepository.findAll();
    }

    public List<VideoPublicitaire> findAllOrderByDateAjoutDesc() {
        return videoPublicitaireRepository.findAllOrderByDateAjoutDesc();
    }

    public Optional<VideoPublicitaire> findById(Integer id) {
        return videoPublicitaireRepository.findById(id);
    }

    public List<VideoPublicitaire> findBySociete(Societe societe) {
        return videoPublicitaireRepository.findBySociete(societe);
    }

    public List<VideoPublicitaire> findBySocieteId(Integer idSociete) {
        return videoPublicitaireRepository.findBySocieteIdSociete(idSociete);
    }

    public List<VideoPublicitaire> findByTitreContaining(String titre) {
        return videoPublicitaireRepository.findByTitreContaining(titre);
    }

    public List<VideoPublicitaire> findByDateAjoutBetween(LocalDate dateDebut, LocalDate dateFin) {
        return videoPublicitaireRepository.findByDateAjoutBetween(dateDebut, dateFin);
    }

    public VideoPublicitaire save(VideoPublicitaire videoPublicitaire) {
        return videoPublicitaireRepository.save(videoPublicitaire);
    }

    public VideoPublicitaire create(VideoPublicitaire videoPublicitaire) {
        if (videoPublicitaire.getDateAjout() == null) {
            videoPublicitaire.setDateAjout(LocalDate.now());
        }
        return videoPublicitaireRepository.save(videoPublicitaire);
    }

    public VideoPublicitaire update(VideoPublicitaire videoPublicitaire) {
        if (videoPublicitaire.getIdVideoPublicitaire() == null) {
            throw new IllegalArgumentException("L'ID de la vidéo ne peut pas être null pour une mise à jour");
        }
        return videoPublicitaireRepository.save(videoPublicitaire);
    }

    public void deleteById(Integer id) {
        videoPublicitaireRepository.deleteById(id);
    }

    public void delete(VideoPublicitaire videoPublicitaire) {
        videoPublicitaireRepository.delete(videoPublicitaire);
    }

    public boolean existsById(Integer id) {
        return videoPublicitaireRepository.existsById(id);
    }

    public long count() {
        return videoPublicitaireRepository.count();
    }
}
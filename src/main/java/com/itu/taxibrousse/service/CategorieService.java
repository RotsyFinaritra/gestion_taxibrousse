package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Categorie;
import com.itu.taxibrousse.repository.CategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;
    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }
    public List<Categorie> findAll() { return categorieRepository.findAll(); }
    public Optional<Categorie> findById(Integer id) { return categorieRepository.findById(id); }
    public Categorie save(Categorie c) { return categorieRepository.save(c); }
    public void deleteById(Integer id) { categorieRepository.deleteById(id); }
}

package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.ClasseCategorie;
import com.itu.taxibrousse.repository.ClasseCategorieRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClasseCategorieService {
    private final ClasseCategorieRepository classeCategorieRepository;
    public ClasseCategorieService(ClasseCategorieRepository classeCategorieRepository) {
        this.classeCategorieRepository = classeCategorieRepository;
    }
    public List<ClasseCategorie> findAll() { return classeCategorieRepository.findAll(); }
    public Optional<ClasseCategorie> findById(Integer id) { return classeCategorieRepository.findById(id); }
    public ClasseCategorie save(ClasseCategorie c) { return classeCategorieRepository.save(c); }
    public void deleteById(Integer id) { classeCategorieRepository.deleteById(id); }
    public Optional<ClasseCategorie> findByClasseAndCategorie(Integer idClasse, Integer idCategorie) {
        if (idClasse == null || idCategorie == null) return Optional.empty();
        return classeCategorieRepository.findByClasse_IdClasseAndCategorie_IdCategorie(idClasse, idCategorie);
    }
    public List<ClasseCategorie> findByCategorie(Integer idCategorie) {
        if (idCategorie == null) return java.util.Collections.emptyList();
        return classeCategorieRepository.findByCategorie_IdCategorie(idCategorie);
    }
}

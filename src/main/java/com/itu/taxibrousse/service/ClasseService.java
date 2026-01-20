package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Classe;
import com.itu.taxibrousse.repository.ClasseRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClasseService {
    private final ClasseRepository classeRepository;
    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }
    public List<Classe> findAll() { return classeRepository.findAll(); }
    public Optional<Classe> findById(Integer id) { return classeRepository.findById(id); }
    public Classe save(Classe c) { return classeRepository.save(c); }
    public void deleteById(Integer id) { classeRepository.deleteById(id); }
}

package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.ClasseSiegeVehicule;
import com.itu.taxibrousse.repository.ClasseSiegeVehiculeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClasseSiegeVehiculeService {
    private final ClasseSiegeVehiculeRepository repository;

    public ClasseSiegeVehiculeService(ClasseSiegeVehiculeRepository repository) {
        this.repository = repository;
    }

    public List<ClasseSiegeVehicule> findAll() { return repository.findAll(); }
    public Optional<ClasseSiegeVehicule> findById(Integer id) { return repository.findById(id); }
    public ClasseSiegeVehicule save(ClasseSiegeVehicule cs) { return repository.save(cs); }
    public void deleteById(Integer id) { repository.deleteById(id); }
    public List<ClasseSiegeVehicule> findByVehicule(Integer idVehicule) { return repository.findByVehicule_IdVehiculeOrderByNumPlace(idVehicule); }
    public java.util.Optional<ClasseSiegeVehicule> findByVehiculeAndNumPlace(Integer idVehicule, Integer numPlace) {
        return repository.findFirstByVehicule_IdVehiculeAndNumPlace(idVehicule, numPlace);
    }
}

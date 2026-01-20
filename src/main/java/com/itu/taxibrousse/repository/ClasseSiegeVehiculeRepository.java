package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.ClasseSiegeVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClasseSiegeVehiculeRepository extends JpaRepository<ClasseSiegeVehicule, Integer> {
    List<ClasseSiegeVehicule> findByVehicule_IdVehicule(Integer idVehicule);
    List<ClasseSiegeVehicule> findByVehicule_IdVehiculeOrderByNumPlace(Integer idVehicule);
    java.util.Optional<ClasseSiegeVehicule> findFirstByVehicule_IdVehiculeAndNumPlace(Integer idVehicule, Integer numPlace);
}

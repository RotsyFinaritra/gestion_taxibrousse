package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, Integer> {
    List<Voyage> findByDateDepart(LocalDate dateDepart);
    List<Voyage> findByTrajet_IdTrajet(Integer idTrajet);
    List<Voyage> findByDateDepartAfterOrderByDateDepartAsc(LocalDate dateDepart);
}

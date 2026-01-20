package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.HeureDepart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface HeureDepartRepository extends JpaRepository<HeureDepart, Integer> {
    List<HeureDepart> findByHeureOrderByHeure(LocalTime heure);
    List<HeureDepart> findAllByOrderByHeureAsc();
}

package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByClient_IdClient(Integer idClient);
    List<Reservation> findByVoyageVehicule_IdVoyageVehicule(Integer idVoyageVehicule);
    List<Reservation> findByDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    List<Reservation> findAllByOrderByDateDesc();
}

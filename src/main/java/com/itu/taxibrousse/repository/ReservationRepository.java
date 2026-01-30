package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer>, ReservationRepositoryCustom {
    List<Reservation> findByClient_IdClient(Integer idClient);
    List<Reservation> findByVoyageVehicule_IdVoyageVehicule(Integer idVoyageVehicule);
    List<Reservation> findByDateBetween(LocalDateTime dateDebut, LocalDateTime dateFin);
    List<Reservation> findAllByOrderByDateDesc();
    List<Reservation> findByEtat(Integer etat);
    
    @Query("SELECT r FROM Reservation r WHERE EXTRACT(MONTH FROM r.date) = :mois AND EXTRACT(YEAR FROM r.date) = :annee")
    List<Reservation> findByMonthAndYear(@Param("mois") Integer mois, @Param("annee") Integer annee);
}

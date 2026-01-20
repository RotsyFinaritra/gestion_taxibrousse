package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.ReservationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Integer> {
    List<ReservationDetails> findByReservation_IdReservation(Integer idReservation);
    List<ReservationDetails> findByReservation_IdReservationOrderByNumPlace(Integer idReservation);
}

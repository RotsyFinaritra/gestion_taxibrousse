package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Reservation;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> findWithFilters(Integer id,
                                      LocalDateTime dateDebut,
                                      LocalDateTime dateFin,
                                      Integer etat,
                                      Integer voyageId,
                                      Integer vehiculeId,
                                      Integer clientId);
}

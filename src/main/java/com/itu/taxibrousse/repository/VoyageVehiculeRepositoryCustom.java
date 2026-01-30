package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VoyageVehicule;
import java.time.LocalDate;
import java.util.List;

public interface VoyageVehiculeRepositoryCustom {
    List<VoyageVehicule> findWithFilters(Integer id,
                                         Integer voyageId,
                                         LocalDate dateDepart,
                                         Integer vehiculeId,
                                         String immatriculation,
                                         String prixOrder);
}

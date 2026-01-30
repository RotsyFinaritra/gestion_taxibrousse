package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Reservation> findWithFilters(Integer id, LocalDateTime dateDebut, LocalDateTime dateFin,
                                             Integer etat, Integer voyageId, Integer vehiculeId, Integer clientId) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT r FROM Reservation r ");
        sb.append("LEFT JOIN r.client c ");
        sb.append("LEFT JOIN r.voyageVehicule vv ");
        sb.append("LEFT JOIN vv.voyage v ");
        sb.append("LEFT JOIN vv.vehicule vh ");

        List<String> clauses = new ArrayList<>();

        if (id != null) clauses.add("r.idReservation = :id");
        if (etat != null) clauses.add("r.etat = :etat");
        if (clientId != null) clauses.add("c.idClient = :clientId");
        if (voyageId != null) clauses.add("v.idVoyage = :voyageId");
        if (vehiculeId != null) clauses.add("vh.idVehicule = :vehiculeId");
        if (dateDebut != null) clauses.add("r.date >= :dateDebut");
        if (dateFin != null) clauses.add("r.date <= :dateFin");

        if (!clauses.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(String.join(" AND ", clauses));
        }

        sb.append(" ORDER BY r.date DESC");

        TypedQuery<Reservation> q = em.createQuery(sb.toString(), Reservation.class);

        if (id != null) q.setParameter("id", id);
        if (etat != null) q.setParameter("etat", etat);
        if (clientId != null) q.setParameter("clientId", clientId);
        if (voyageId != null) q.setParameter("voyageId", voyageId);
        if (vehiculeId != null) q.setParameter("vehiculeId", vehiculeId);
        if (dateDebut != null) q.setParameter("dateDebut", dateDebut);
        if (dateFin != null) q.setParameter("dateFin", dateFin);

        return q.getResultList();
    }
}

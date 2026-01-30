package com.itu.taxibrousse.repository;

import com.itu.taxibrousse.entity.VoyageVehicule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VoyageVehiculeRepositoryImpl implements VoyageVehiculeRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<VoyageVehicule> findWithFilters(Integer id, Integer voyageId, LocalDate dateDepart,
                                                Integer vehiculeId, String immatriculation, String prixOrder) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT vv FROM VoyageVehicule vv ");
        sb.append("LEFT JOIN vv.voyage v ");
        sb.append("LEFT JOIN vv.vehicule vh ");

        List<String> clauses = new ArrayList<>();

        if (id != null) clauses.add("vv.idVoyageVehicule = :id");
        if (voyageId != null) clauses.add("v.idVoyage = :voyageId");
        if (dateDepart != null) clauses.add("v.dateDepart = :dateDepart");
        if (vehiculeId != null) clauses.add("vh.idVehicule = :vehiculeId");
        if (immatriculation != null && !immatriculation.trim().isEmpty()) clauses.add("LOWER(vh.immatriculation) LIKE :immatriculation");

        if (!clauses.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(String.join(" AND ", clauses));
        }

        if ("asc".equalsIgnoreCase(prixOrder)) {
            sb.append(" ORDER BY vv.prix ASC");
        } else if ("desc".equalsIgnoreCase(prixOrder)) {
            sb.append(" ORDER BY vv.prix DESC");
        } else {
            sb.append(" ORDER BY vv.idVoyageVehicule DESC");
        }

        TypedQuery<VoyageVehicule> q = em.createQuery(sb.toString(), VoyageVehicule.class);

        if (id != null) q.setParameter("id", id);
        if (voyageId != null) q.setParameter("voyageId", voyageId);
        if (dateDepart != null) q.setParameter("dateDepart", dateDepart);
        if (vehiculeId != null) q.setParameter("vehiculeId", vehiculeId);
        if (immatriculation != null && !immatriculation.trim().isEmpty()) q.setParameter("immatriculation", "%" + immatriculation.trim().toLowerCase() + "%");

        return q.getResultList();
    }
}

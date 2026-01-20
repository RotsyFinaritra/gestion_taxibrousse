package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.ReservationDetails;
import com.itu.taxibrousse.repository.ReservationDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.itu.taxibrousse.entity.VoyageVehicule;
import com.itu.taxibrousse.entity.ClasseCategorie;
import com.itu.taxibrousse.entity.Categorie;
import com.itu.taxibrousse.service.ReservationService;
import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationDetailsService {

    private final ReservationDetailsRepository reservationDetailsRepository;

    public ReservationDetailsService(ReservationDetailsRepository reservationDetailsRepository) {
        this.reservationDetailsRepository = reservationDetailsRepository;
    }

    @Autowired
    @Lazy
    private ReservationService reservationService;

    public List<ReservationDetails> findAll() {
        return reservationDetailsRepository.findAll();
    }

    public Optional<ReservationDetails> findById(Integer id) {
        return reservationDetailsRepository.findById(id);
    }

    public ReservationDetails save(ReservationDetails details) {
        return reservationDetailsRepository.save(details);
    }

    public void deleteById(Integer id) {
        reservationDetailsRepository.deleteById(id);
    }

    public List<ReservationDetails> findByReservation(Integer idReservation) {
        return reservationDetailsRepository.findByReservation_IdReservationOrderByNumPlace(idReservation);
    }

    /**
     * Compute the montant for a ReservationDetails using the same precedence logic
     * as used elsewhere (specials, classe+categorie, classe, voyage/voyageVehicule/trajet).
     * Returns a BigDecimal scaled to 2 decimals (or zero if not computable).
     */
    public BigDecimal getMontantCalculated(ReservationDetails rd, VoyageVehicule vv) {
        if (rd == null) return BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP);
        try {
            ClasseCategorie cc = rd.getClasseCategorie();
            Categorie cat = cc != null ? cc.getCategorie() : null;
            ReservationService.PriceResult pr = null;
            if (reservationService != null) {
                pr = reservationService.computePriceForSeatWithSource(cc, cat, vv);
            }
            if (pr != null && pr.price != null) return pr.price.setScale(2, java.math.RoundingMode.HALF_UP);
            if (rd.getMontant() != null) return BigDecimal.valueOf(rd.getMontant()).setScale(2, java.math.RoundingMode.HALF_UP);
        } catch (Exception ignore) {}
        return BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}

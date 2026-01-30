package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.service.ReservationService;
import com.itu.taxibrousse.service.VoyageService;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.DiffusionPubDetailsService;
import com.itu.taxibrousse.entity.VoyageVehicule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class RevenueReportAdminController {

    private final VoyageService voyageService;
    private final VoyageVehiculeService voyageVehiculeService;
    private final ReservationService reservationService;
    private final DiffusionPubDetailsService diffusionPubDetailsService;

    public RevenueReportAdminController(VoyageService voyageService,
                                        VoyageVehiculeService voyageVehiculeService,
                                        ReservationService reservationService,
                                        DiffusionPubDetailsService diffusionPubDetailsService) {
        this.voyageService = voyageService;
        this.voyageVehiculeService = voyageVehiculeService;
        this.reservationService = reservationService;
        this.diffusionPubDetailsService = diffusionPubDetailsService;
    }

    @GetMapping("/revenue")
    public String revenuePage(Model model) {
        model.addAttribute("voyages", voyageService.findAll());
        return "backoffice/reports/revenue";
    }

    @GetMapping("/advertising")
    public String advertisingPage(Model model) {
        java.time.LocalDate now = java.time.LocalDate.now();
        model.addAttribute("currentMonth", now.getMonthValue());
        model.addAttribute("currentYear", now.getYear());
        return "backoffice/reports/advertising";
    }

    @GetMapping(value = "/voyageVehicules", produces = "application/json")
    @ResponseBody
    public List<VoyageVehicule> getVoyageVehicules(@RequestParam("voyageId") Integer voyageId) {
        return voyageVehiculeService.findByVoyage(voyageId);
    }

    @GetMapping(value = "/revenue/compute", produces = "application/json")
    @ResponseBody
    public Map<String, Object> computeRevenue(@RequestParam(value = "voyageVehiculeId", required = false) Integer voyageVehiculeId,
                                            @RequestParam(value = "mois", required = false) Integer mois,
                                            @RequestParam(value = "annee", required = false) Integer annee) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            BigDecimal revenueReservations = BigDecimal.ZERO;
            BigDecimal revenuePublicite = BigDecimal.ZERO;

            if (voyageVehiculeId != null) {
                // Calcul pour un voyage-véhicule spécifique
                revenueReservations = reservationService.calculateRevenue(voyageVehiculeId);
                revenuePublicite = diffusionPubDetailsService.calculateRevenue(voyageVehiculeId);
            } else if (mois != null && annee != null) {
                // Calcul pour un mois et une année
                revenueReservations = reservationService.calculateRevenueByMonth(mois, annee);
                revenuePublicite = diffusionPubDetailsService.calculateRevenueByMonth(mois, annee);
            } else {
                // Calcul global
                revenueReservations = reservationService.getTotalRevenue();
                revenuePublicite = diffusionPubDetailsService.getTotalRevenue();
            }

            result.put("revenueReservations", revenueReservations);
            result.put("revenuePublicite", revenuePublicite);
            result.put("totalRevenue", revenueReservations.add(revenuePublicite));
            result.put("success", true);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Erreur lors du calcul du chiffre d'affaires: " + e.getMessage());
        }

        return result;
    }

    @GetMapping(value = "/advertising/compute", produces = "application/json")
    @ResponseBody
    public Map<String, Object> computeAdvertising(@RequestParam("mois") Integer mois,
                                                   @RequestParam("annee") Integer annee) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            BigDecimal revenuePublicite = diffusionPubDetailsService.calculateRevenueByMonth(mois, annee);
            
            if (revenuePublicite == null) {
                revenuePublicite = BigDecimal.ZERO;
            }

            result.put("revenuePublicite", revenuePublicite);
            result.put("success", true);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Erreur lors du calcul du chiffre d'affaires publicitaire: " + e.getMessage());
            result.put("revenuePublicite", BigDecimal.ZERO);
        }

        return result;
    }
}
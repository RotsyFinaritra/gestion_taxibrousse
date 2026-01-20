package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.service.ReservationService;
import com.itu.taxibrousse.service.VoyageService;
import com.itu.taxibrousse.service.VoyageVehiculeService;
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

    public RevenueReportAdminController(VoyageService voyageService,
                                        VoyageVehiculeService voyageVehiculeService,
                                        ReservationService reservationService) {
        this.voyageService = voyageService;
        this.voyageVehiculeService = voyageVehiculeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/revenue")
    public String revenuePage(Model model) {
        model.addAttribute("voyages", voyageService.findAll());
        return "backoffice/reports/revenue";
    }

    @GetMapping(value = "/voyageVehicules", produces = "application/json")
    @ResponseBody
    public List<VoyageVehicule> getVoyageVehicules(@RequestParam("voyageId") Integer voyageId) {
        return voyageVehiculeService.findByVoyage(voyageId);
    }

    @GetMapping(value = "/revenue/compute", produces = "application/json")
    @ResponseBody
    public Map<String, Object> computeRevenue(@RequestParam("voyageVehiculeId") Integer voyageVehiculeId) {
        Map<String, Object> resp = new HashMap<>();
        try {
            java.util.List<com.itu.taxibrousse.entity.Reservation> reservations = reservationService.findByVoyageVehicule(voyageVehiculeId);
            java.math.BigDecimal total = BigDecimal.ZERO;
            int count = 0;
            for (com.itu.taxibrousse.entity.Reservation r : reservations) {
                BigDecimal mont = reservationService.getMontantTotal(r);
                if (mont != null) {
                    total = total.add(mont);
                }
                count++;
            }
            resp.put("success", true);
            resp.put("total", total.setScale(2, java.math.RoundingMode.HALF_UP).toString());
            resp.put("count", count);
        } catch (Exception ex) {
            resp.put("success", false);
            resp.put("error", ex.getMessage());
            resp.put("total", "0.00");
            resp.put("count", 0);
        }
        return resp;
    }

}

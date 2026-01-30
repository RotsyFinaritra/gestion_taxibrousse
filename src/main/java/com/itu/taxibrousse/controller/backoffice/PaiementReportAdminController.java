package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.service.PaiementDiffusionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reports/paiements")
public class PaiementReportAdminController {

    private final PaiementDiffusionService paiementDiffusionService;

    public PaiementReportAdminController(PaiementDiffusionService paiementDiffusionService) {
        this.paiementDiffusionService = paiementDiffusionService;
    }

    @GetMapping
    public String rapportPaiements(@RequestParam(value = "mois", required = false) Integer mois,
                                  @RequestParam(value = "annee", required = false) Integer annee,
                                  Model model) {
        
        // Valeurs par défaut : mois/année courants
        LocalDate maintenant = LocalDate.now();
        if (mois == null) {
            mois = maintenant.getMonthValue();
        }
        if (annee == null) {
            annee = maintenant.getYear();
        }
        
        // Validation des paramètres
        if (mois < 1 || mois > 12) {
            model.addAttribute("error", "Le mois doit être compris entre 1 et 12");
            mois = maintenant.getMonthValue();
        }
        if (annee < 2000 || annee > 2100) {
            model.addAttribute("error", "L'année doit être comprise entre 2000 et 2100");
            annee = maintenant.getYear();
        }
        
        // Générer le rapport
        var rapport = paiementDiffusionService.getRapportPaiements(mois, annee);
        
        // Calculer les totaux
        var totalDu = rapport.stream()
            .map(PaiementDiffusionService.RapportPaiementDiffusion::getMontantDu)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        var totalPaye = rapport.stream()
            .map(PaiementDiffusionService.RapportPaiementDiffusion::getMontantPaye)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        var totalRestant = rapport.stream()
            .map(PaiementDiffusionService.RapportPaiementDiffusion::getMontantRestant)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        
        model.addAttribute("rapport", rapport);
        model.addAttribute("moisSelectionne", mois);
        model.addAttribute("anneeSelectionnee", annee);
        model.addAttribute("totalDu", totalDu);
        model.addAttribute("totalPaye", totalPaye);
        model.addAttribute("totalRestant", totalRestant);
        
        // Noms des mois pour l'affichage
        String[] nomsMois = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        model.addAttribute("nomMois", nomsMois[mois - 1]);
        
        return "backoffice/reports/paiements";
    }
}

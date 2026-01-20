package com.itu.taxibrousse.controller.frontoffice;

import com.itu.taxibrousse.service.TrajetService;
import com.itu.taxibrousse.service.VilleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trajets")
public class TrajetClientController {
    
    private final TrajetService trajetService;
    private final VilleService villeService;
    
    public TrajetClientController(TrajetService trajetService, VilleService villeService) {
        this.trajetService = trajetService;
        this.villeService = villeService;
    }
    
    @GetMapping("/recherche")
    public String recherche(Model model) {
        model.addAttribute("villes", villeService.findAll());
        return "frontoffice/recherche";
    }
    
    @GetMapping("/resultats")
    public String resultats(
            @RequestParam(required = false) Integer villeDepart,
            @RequestParam(required = false) Integer villeArrivee,
            Model model) {
        
        if (villeDepart != null && villeArrivee != null) {
            model.addAttribute("trajets", trajetService.findByVillesDepartEtArrivee(villeDepart, villeArrivee));
        } else {
            model.addAttribute("trajets", trajetService.findAll());
        }
        
        model.addAttribute("villes", villeService.findAll());
        return "frontoffice/resultats";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Voyage;
import com.itu.taxibrousse.service.VoyageService;
import com.itu.taxibrousse.service.HeureDepartService;
import com.itu.taxibrousse.service.TrajetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/voyages")
public class VoyageAdminController {
    
    private final VoyageService voyageService;
    private final HeureDepartService heureDepartService;
    private final TrajetService trajetService;
    
    public VoyageAdminController(VoyageService voyageService, HeureDepartService heureDepartService, TrajetService trajetService) {
        this.voyageService = voyageService;
        this.heureDepartService = heureDepartService;
        this.trajetService = trajetService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("voyages", voyageService.findAll());
        return "backoffice/voyages/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("voyage", new Voyage());
        model.addAttribute("heures", heureDepartService.findAll());
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/voyages/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return voyageService.findById(id)
            .map(voyage -> {
                model.addAttribute("voyage", voyage);
                model.addAttribute("heures", heureDepartService.findAll());
                model.addAttribute("trajets", trajetService.findAll());
                return "backoffice/voyages/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Voyage non trouvé");
                return "redirect:/admin/voyages";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute Voyage voyage, RedirectAttributes redirectAttributes) {
        try {
            voyageService.save(voyage);
            redirectAttributes.addFlashAttribute("success", "Voyage enregistré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du voyage");
        }
        return "redirect:/admin/voyages";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            voyageService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Voyage supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du voyage");
        }
        return "redirect:/admin/voyages";
    }
}

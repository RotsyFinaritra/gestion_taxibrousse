package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.HeureDepart;
import com.itu.taxibrousse.service.HeureDepartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/heures")
public class HeureDepartAdminController {
    
    private final HeureDepartService heureDepartService;
    
    public HeureDepartAdminController(HeureDepartService heureDepartService) {
        this.heureDepartService = heureDepartService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("heures", heureDepartService.findAll());
        return "backoffice/heures/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("heureDepart", new HeureDepart());
        return "backoffice/heures/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return heureDepartService.findById(id)
            .map(heureDepart -> {
                model.addAttribute("heureDepart", heureDepart);
                return "backoffice/heures/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Heure de départ non trouvée");
                return "redirect:/admin/heures";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute HeureDepart heureDepart, RedirectAttributes redirectAttributes) {
        try {
            heureDepartService.save(heureDepart);
            redirectAttributes.addFlashAttribute("success", "Heure de départ enregistrée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement de l'heure de départ");
        }
        return "redirect:/admin/heures";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            heureDepartService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Heure de départ supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression de l'heure de départ");
        }
        return "redirect:/admin/heures";
    }
}

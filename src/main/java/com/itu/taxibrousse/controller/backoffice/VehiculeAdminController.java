package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Vehicule;
import com.itu.taxibrousse.service.VehiculeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/vehicules")
public class VehiculeAdminController {
    
    private final VehiculeService vehiculeService;
    
    public VehiculeAdminController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("vehicules", vehiculeService.findAll());
        return "backoffice/vehicules/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("vehicule", new Vehicule());
        return "backoffice/vehicules/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return vehiculeService.findById(id)
            .map(vehicule -> {
                model.addAttribute("vehicule", vehicule);
                return "backoffice/vehicules/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Véhicule non trouvé");
                return "redirect:/admin/vehicules";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute Vehicule vehicule, RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.save(vehicule);
            redirectAttributes.addFlashAttribute("success", "Véhicule enregistré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du véhicule");
        }
        return "redirect:/admin/vehicules";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            vehiculeService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Véhicule supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du véhicule");
        }
        return "redirect:/admin/vehicules";
    }
}

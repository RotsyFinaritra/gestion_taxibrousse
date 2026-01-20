package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.VoyageVehicule;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.VoyageService;
import com.itu.taxibrousse.service.VehiculeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/voyage-vehicules")
public class VoyageVehiculeAdminController {
    
    private final VoyageVehiculeService voyageVehiculeService;
    private final VoyageService voyageService;
    private final VehiculeService vehiculeService;
    
    public VoyageVehiculeAdminController(VoyageVehiculeService voyageVehiculeService, 
                                         VoyageService voyageService, 
                                         VehiculeService vehiculeService) {
        this.voyageVehiculeService = voyageVehiculeService;
        this.voyageService = voyageService;
        this.vehiculeService = vehiculeService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        return "backoffice/voyage-vehicules/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("voyageVehicule", new VoyageVehicule());
        model.addAttribute("voyages", voyageService.findAll());
        model.addAttribute("vehicules", vehiculeService.findAll());
        return "backoffice/voyage-vehicules/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return voyageVehiculeService.findById(id)
            .map(voyageVehicule -> {
                model.addAttribute("voyageVehicule", voyageVehicule);
                model.addAttribute("voyages", voyageService.findAll());
                model.addAttribute("vehicules", vehiculeService.findAll());
                return "backoffice/voyage-vehicules/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Voyage-Véhicule non trouvé");
                return "redirect:/admin/voyage-vehicules";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute VoyageVehicule voyageVehicule, RedirectAttributes redirectAttributes) {
        try {
            voyageVehiculeService.save(voyageVehicule);
            redirectAttributes.addFlashAttribute("success", "Voyage-Véhicule enregistré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du voyage-véhicule");
        }
        return "redirect:/admin/voyage-vehicules";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            voyageVehiculeService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Voyage-Véhicule supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du voyage-véhicule");
        }
        return "redirect:/admin/voyage-vehicules";
    }
}

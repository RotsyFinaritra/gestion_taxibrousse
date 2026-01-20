package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.GareRoutiere;
import com.itu.taxibrousse.service.GareRoutiereService;
import com.itu.taxibrousse.service.VilleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/gares")
public class GareRoutiereAdminController {
    
    private final GareRoutiereService gareRoutiereService;
    private final VilleService villeService;
    
    public GareRoutiereAdminController(GareRoutiereService gareRoutiereService, VilleService villeService) {
        this.gareRoutiereService = gareRoutiereService;
        this.villeService = villeService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("gares", gareRoutiereService.findAll());
        return "backoffice/gares/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("gare", new GareRoutiere());
        model.addAttribute("villes", villeService.findAll());
        return "backoffice/gares/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return gareRoutiereService.findById(id)
            .map(gare -> {
                model.addAttribute("gare", gare);
                model.addAttribute("villes", villeService.findAll());
                return "backoffice/gares/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Gare routière non trouvée");
                return "redirect:/admin/gares";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute GareRoutiere gare, RedirectAttributes redirectAttributes) {
        gareRoutiereService.save(gare);
        redirectAttributes.addFlashAttribute("success", "Gare routière enregistrée avec succès");
        return "redirect:/admin/gares";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        gareRoutiereService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Gare routière supprimée avec succès");
        return "redirect:/admin/gares";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Ville;
import com.itu.taxibrousse.service.VilleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/villes")
public class VilleAdminController {
    
    private final VilleService villeService;
    
    public VilleAdminController(VilleService villeService) {
        this.villeService = villeService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("villes", villeService.findAll());
        return "backoffice/villes/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("ville", new Ville());
        return "backoffice/villes/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return villeService.findById(id)
            .map(ville -> {
                model.addAttribute("ville", ville);
                return "backoffice/villes/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Ville non trouvée");
                return "redirect:/admin/villes";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute Ville ville, RedirectAttributes redirectAttributes) {
        villeService.save(ville);
        redirectAttributes.addFlashAttribute("success", "Ville enregistrée avec succès");
        return "redirect:/admin/villes";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        villeService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Ville supprimée avec succès");
        return "redirect:/admin/villes";
    }
}

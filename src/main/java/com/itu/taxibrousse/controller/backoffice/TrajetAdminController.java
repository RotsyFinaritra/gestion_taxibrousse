package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Trajet;
import com.itu.taxibrousse.service.GareRoutiereService;
import com.itu.taxibrousse.service.TrajetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/trajets")
public class TrajetAdminController {

    private final TrajetService trajetService;
    private final GareRoutiereService gareRoutiereService;

    public TrajetAdminController(TrajetService trajetService, GareRoutiereService gareRoutiereService) {
        this.trajetService = trajetService;
        this.gareRoutiereService = gareRoutiereService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/trajets/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("trajet", new Trajet());
        model.addAttribute("gares", gareRoutiereService.findAll());
        return "backoffice/trajets/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return trajetService.findById(id)
                .map(trajet -> {
                    model.addAttribute("trajet", trajet);
                    model.addAttribute("gares", gareRoutiereService.findAll());
                    return "backoffice/trajets/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Trajet non trouvé");
                    return "redirect:/admin/trajets";
                });
    }

    @PostMapping
    public String save(@ModelAttribute Trajet trajet, RedirectAttributes redirectAttributes) {
        trajetService.save(trajet);
        redirectAttributes.addFlashAttribute("success", "Trajet enregistré avec succès");
        return "redirect:/admin/trajets";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        trajetService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Trajet supprimé avec succès");
        return "redirect:/admin/trajets";
    }
}

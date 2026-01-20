package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.TarifClasseTrajet;
import com.itu.taxibrousse.service.TarifClasseTrajetService;
import com.itu.taxibrousse.service.ClasseService;
import com.itu.taxibrousse.service.TrajetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tarif-classe-trajet")
public class TarifClasseTrajetAdminController {
    private final TarifClasseTrajetService service;
    private final ClasseService classeService;
    private final TrajetService trajetService;

    public TarifClasseTrajetAdminController(TarifClasseTrajetService service, ClasseService classeService, TrajetService trajetService) {
        this.service = service;
        this.classeService = classeService;
        this.trajetService = trajetService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tarifs", service.findAll());
        return "backoffice/tarifclassetrajet/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("tarif", new TarifClasseTrajet());
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/tarifclassetrajet/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TarifClasseTrajet tarif, RedirectAttributes ra) {
        service.save(tarif);
        ra.addFlashAttribute("success", "Tarif enregistré");
        return "redirect:/admin/tarif-classe-trajet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("tarif", service.findById(id).orElse(new TarifClasseTrajet()));
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/tarifclassetrajet/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute TarifClasseTrajet tarif, RedirectAttributes ra) {
        service.save(tarif);
        ra.addFlashAttribute("success", "Tarif modifié");
        return "redirect:/admin/tarif-classe-trajet";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Tarif supprimé");
        return "redirect:/admin/tarif-classe-trajet";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.TarifClasseCategorieTrajet;
import com.itu.taxibrousse.service.TarifClasseCategorieTrajetService;
import com.itu.taxibrousse.service.ClasseCategorieService;
import com.itu.taxibrousse.service.TrajetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tarif-classe-categorie-trajet")
public class TarifClasseCategorieTrajetAdminController {
    private final TarifClasseCategorieTrajetService service;
    private final ClasseCategorieService classeCategorieService;
    private final TrajetService trajetService;

    public TarifClasseCategorieTrajetAdminController(TarifClasseCategorieTrajetService service, ClasseCategorieService classeCategorieService, TrajetService trajetService) {
        this.service = service;
        this.classeCategorieService = classeCategorieService;
        this.trajetService = trajetService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tarifs", service.findAll());
        return "backoffice/tarifclassecategoriatrajet/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("tarif", new TarifClasseCategorieTrajet());
        model.addAttribute("classeCategories", classeCategorieService.findAll());
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/tarifclassecategoriatrajet/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TarifClasseCategorieTrajet tarif, RedirectAttributes ra) {
        try {
            service.save(tarif);
            ra.addFlashAttribute("success", "Tarif enregistré");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/tarif-classe-categorie-trajet/new";
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Erreur lors de l'enregistrement du tarif: " + ex.getMessage());
        }
        return "redirect:/admin/tarif-classe-categorie-trajet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("tarif", service.findById(id).orElse(new TarifClasseCategorieTrajet()));
        model.addAttribute("classeCategories", classeCategorieService.findAll());
        model.addAttribute("trajets", trajetService.findAll());
        return "backoffice/tarifclassecategoriatrajet/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute TarifClasseCategorieTrajet tarif, RedirectAttributes ra) {
        try {
            service.save(tarif);
            ra.addFlashAttribute("success", "Tarif modifié");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/tarif-classe-categorie-trajet/" + (tarif.getIdTarifClasseCategorieTrajet() != null ? tarif.getIdTarifClasseCategorieTrajet() + "/edit" : "new");
        } catch (Exception ex) {
            ra.addFlashAttribute("error", "Erreur lors de la modification du tarif: " + ex.getMessage());
        }
        return "redirect:/admin/tarif-classe-categorie-trajet";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Tarif supprimé");
        return "redirect:/admin/tarif-classe-categorie-trajet";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Societe;
import com.itu.taxibrousse.service.SocieteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/societes")
public class SocieteAdminController {

    private final SocieteService societeService;

    public SocieteAdminController(SocieteService societeService) {
        this.societeService = societeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("societes", societeService.findAllOrderByNom());
        return "backoffice/societes/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("societe", new Societe());
        return "backoffice/societes/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Societe societe, RedirectAttributes ra) {
        try {
            if (societe.getDateCreation() == null) {
                societe.setDateCreation(LocalDate.now());
            }
            societeService.save(societe);
            ra.addFlashAttribute("successMessage", "Société créée avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la création de la société : " + e.getMessage());
        }
        return "redirect:/admin/societes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Societe societe = societeService.findById(id).orElseThrow(() -> 
                new RuntimeException("Société non trouvée avec l'ID : " + id));
            model.addAttribute("societe", societe);
            return "backoffice/societes/form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Société non trouvée : " + e.getMessage());
            return "redirect:/admin/societes";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Societe societe, RedirectAttributes ra) {
        try {
            societeService.update(societe);
            ra.addFlashAttribute("successMessage", "Société mise à jour avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/societes";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            if (!societeService.existsById(id)) {
                throw new RuntimeException("Société non trouvée avec l'ID : " + id);
            }
            societeService.deleteById(id);
            ra.addFlashAttribute("successMessage", "Société supprimée avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/societes";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Societe societe = societeService.findById(id).orElseThrow(() -> 
                new RuntimeException("Société non trouvée avec l'ID : " + id));
            model.addAttribute("societe", societe);
            return "backoffice/societes/view";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Société non trouvée : " + e.getMessage());
            return "redirect:/admin/societes";
        }
    }
}
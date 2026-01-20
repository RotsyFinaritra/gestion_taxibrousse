package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Categorie;
import com.itu.taxibrousse.service.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class CategorieAdminController {
    private final CategorieService categorieService;
    public CategorieAdminController(CategorieService categorieService) { this.categorieService = categorieService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categorieService.findAll());
        return "backoffice/categorie/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("categorie", new Categorie());
        return "backoffice/categorie/form";
    }

    @PostMapping
    public String save(@ModelAttribute Categorie categorie, RedirectAttributes redirectAttributes) {
        categorieService.save(categorie);
        redirectAttributes.addFlashAttribute("success", "Catégorie enregistrée");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("categorie", categorieService.findById(id).orElse(new Categorie()));
        return "backoffice/categorie/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        categorieService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Catégorie supprimée");
        return "redirect:/admin/categories";
    }
}

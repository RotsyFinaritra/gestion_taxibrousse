package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.TarifSpecialCategorie;
import com.itu.taxibrousse.service.TarifSpecialCategorieService;
import com.itu.taxibrousse.service.ClasseCategorieService;
import com.itu.taxibrousse.service.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tarif-special-categorie")
public class TarifSpecialCategorieAdminController {
    private final TarifSpecialCategorieService service;
    private final ClasseCategorieService classeCategorieService;
    private final CategorieService categorieService;

    public TarifSpecialCategorieAdminController(TarifSpecialCategorieService service,
                                               ClasseCategorieService classeCategorieService,
                                               CategorieService categorieService) {
        this.service = service;
        this.classeCategorieService = classeCategorieService;
        this.categorieService = categorieService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tarifs", service.findAll());
        return "backoffice/tarifspecialcategorie/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("tarif", new TarifSpecialCategorie());
        model.addAttribute("classeCategories", classeCategorieService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        return "backoffice/tarifspecialcategorie/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute TarifSpecialCategorie tarif, RedirectAttributes ra) {
        service.save(tarif);
        ra.addFlashAttribute("success", "Tarif enregistré");
        return "redirect:/admin/tarif-special-categorie";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("tarif", service.findById(id).orElse(new TarifSpecialCategorie()));
        model.addAttribute("classeCategories", classeCategorieService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        return "backoffice/tarifspecialcategorie/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute TarifSpecialCategorie tarif, RedirectAttributes ra) {
        service.save(tarif);
        ra.addFlashAttribute("success", "Tarif modifié");
        return "redirect:/admin/tarif-special-categorie";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.deleteById(id);
        ra.addFlashAttribute("success", "Tarif supprimé");
        return "redirect:/admin/tarif-special-categorie";
    }
}

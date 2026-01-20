package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.ClasseCategorie;
import com.itu.taxibrousse.service.ClasseCategorieService;
import com.itu.taxibrousse.service.ClasseService;
import com.itu.taxibrousse.service.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/classecategories")
public class ClasseCategorieAdminController {
    private final ClasseCategorieService classeCategorieService;
    private final ClasseService classeService;
    private final CategorieService categorieService;
    public ClasseCategorieAdminController(ClasseCategorieService classeCategorieService, ClasseService classeService, CategorieService categorieService) {
        this.classeCategorieService = classeCategorieService;
        this.classeService = classeService;
        this.categorieService = categorieService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classeCategories", classeCategorieService.findAll());
        return "backoffice/classecategorie/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("classeCategorie", new ClasseCategorie());
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        return "backoffice/classecategorie/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ClasseCategorie classeCategorie, RedirectAttributes redirectAttributes) {
        classeCategorieService.save(classeCategorie);
        redirectAttributes.addFlashAttribute("success", "Association enregistrée");
        return "redirect:/admin/classecategories";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute ClasseCategorie classeCategorie, RedirectAttributes redirectAttributes) {
        classeCategorieService.save(classeCategorie);
        redirectAttributes.addFlashAttribute("success", "Association modifiée");
        return "redirect:/admin/classecategories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("classeCategorie", classeCategorieService.findById(id).orElse(new ClasseCategorie()));
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        return "backoffice/classecategorie/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        classeCategorieService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Association supprimée");
        return "redirect:/admin/classecategories";
    }
}

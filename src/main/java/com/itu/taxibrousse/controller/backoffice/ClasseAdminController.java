package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Classe;
import com.itu.taxibrousse.service.ClasseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/classes")
public class ClasseAdminController {
    private final ClasseService classeService;
    public ClasseAdminController(ClasseService classeService) { this.classeService = classeService; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", classeService.findAll());
        return "backoffice/classe/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("classe", new Classe());
        return "backoffice/classe/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Classe classe, RedirectAttributes redirectAttributes) {
        classeService.save(classe);
        redirectAttributes.addFlashAttribute("success", "Classe enregistrée");
        return "redirect:/admin/classes";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("classe", classeService.findById(id).orElse(new Classe()));
        return "backoffice/classe/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Classe classe, RedirectAttributes redirectAttributes) {
        classeService.save(classe);
        redirectAttributes.addFlashAttribute("success", "Classe modifiée");
        return "redirect:/admin/classes";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        classeService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Classe supprimée");
        return "redirect:/admin/classes";
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Client;
import com.itu.taxibrousse.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/clients")
public class ClientAdminController {
    
    private final ClientService clientService;
    
    public ClientAdminController(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "backoffice/clients/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("client", new Client());
        return "backoffice/clients/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return clientService.findById(id)
            .map(client -> {
                model.addAttribute("client", client);
                return "backoffice/clients/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Client non trouvé");
                return "redirect:/admin/clients";
            });
    }
    
    @PostMapping
    public String save(@ModelAttribute Client client, RedirectAttributes redirectAttributes) {
        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("success", "Client enregistré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement du client");
        }
        return "redirect:/admin/clients";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Client supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression du client");
        }
        return "redirect:/admin/clients";
    }
}

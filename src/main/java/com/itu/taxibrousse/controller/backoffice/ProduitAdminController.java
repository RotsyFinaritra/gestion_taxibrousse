package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.entity.HistoriquePrixProduit;
import com.itu.taxibrousse.service.ProduitService;
import com.itu.taxibrousse.service.HistoriquePrixProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/produits")
public class ProduitAdminController {
    
    private final ProduitService produitService;
    private final HistoriquePrixProduitService historiquePrixProduitService;
    
    public ProduitAdminController(ProduitService produitService, 
                                  HistoriquePrixProduitService historiquePrixProduitService) {
        this.produitService = produitService;
        this.historiquePrixProduitService = historiquePrixProduitService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("produits", produitService.findAll());
        return "backoffice/produits/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "backoffice/produits/form";
    }

    @PostMapping
    public String save(@ModelAttribute Produit produit, RedirectAttributes redirectAttributes) {
        Produit saved = produitService.save(produit);
        
        // Créer automatiquement une entrée dans l'historique des prix
        if (saved.getPrix() != null && saved.getPrix().compareTo(java.math.BigDecimal.ZERO) > 0) {
            HistoriquePrixProduit historique = new HistoriquePrixProduit();
            historique.setProduit(saved);
            historique.setMontant(saved.getPrix());
            historique.setDate(LocalDate.now());
            historiquePrixProduitService.save(historique);
        }
        
        redirectAttributes.addFlashAttribute("success", "Produit enregistré");
        return "redirect:/admin/produits";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("produit", produitService.findById(id).orElse(new Produit()));
        return "backoffice/produits/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        produitService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Produit supprimé");
        return "redirect:/admin/produits";
    }
    
    @GetMapping("/{id}/historique")
    public String historique(@PathVariable Integer id, Model model) {
        model.addAttribute("produit", produitService.findById(id).orElse(null));
        model.addAttribute("historique", historiquePrixProduitService.findByProduitId(id));
        return "backoffice/produits/historique";
    }
}

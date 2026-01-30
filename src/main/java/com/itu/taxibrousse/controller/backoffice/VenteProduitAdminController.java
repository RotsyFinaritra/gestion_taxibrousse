package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.entity.VenteDetails;
import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.service.VenteProduitService;
import com.itu.taxibrousse.service.VenteDetailsService;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.ProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/vente-produits")
public class VenteProduitAdminController {
    
    private final VenteProduitService venteProduitService;
    private final VenteDetailsService venteDetailsService;
    private final VoyageVehiculeService voyageVehiculeService;
    private final ProduitService produitService;
    
    public VenteProduitAdminController(VenteProduitService venteProduitService,
                                       VenteDetailsService venteDetailsService,
                                       VoyageVehiculeService voyageVehiculeService,
                                       ProduitService produitService) {
        this.venteProduitService = venteProduitService;
        this.venteDetailsService = venteDetailsService;
        this.voyageVehiculeService = voyageVehiculeService;
        this.produitService = produitService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("ventes", venteProduitService.findAll());
        return "backoffice/vente-produits/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("venteProduit", new VenteProduit());
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        model.addAttribute("produits", produitService.findAll());
        return "backoffice/vente-produits/form";
    }

    @PostMapping
    public String save(@ModelAttribute VenteProduit venteProduit,
                      HttpServletRequest request,
                      RedirectAttributes redirectAttributes) {
        
        // Définir la date actuelle si non fournie
        if (venteProduit.getDate() == null) {
            venteProduit.setDate(LocalDate.now());
        }
        
        // Si c'est une modification, supprimer les anciens détails
        if (venteProduit.getIdVenteProduit() != null) {
            List<VenteDetails> existingDetails = venteDetailsService.findByVenteProduitId(venteProduit.getIdVenteProduit());
            for (VenteDetails detail : existingDetails) {
                venteDetailsService.deleteById(detail.getIdVenteDetails());
            }
        }
        
        VenteProduit saved = venteProduitService.save(venteProduit);
        
        // Récupérer les quantités depuis les paramètres de la requête
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            // Vérifier si le paramètre correspond au pattern quantites[id]
            if (paramName.startsWith("quantites[") && paramName.endsWith("]")) {
                try {
                    // Extraire l'ID du produit
                    String idStr = paramName.substring("quantites[".length(), paramName.length() - 1);
                    Integer produitId = Integer.parseInt(idStr);
                    
                    // Récupérer la quantité
                    String[] values = entry.getValue();
                    if (values != null && values.length > 0) {
                        Integer quantite = Integer.parseInt(values[0]);
                        
                        if (quantite != null && quantite > 0) {
                            Produit produit = produitService.findById(produitId).orElse(null);
                            if (produit != null) {
                                VenteDetails detail = new VenteDetails();
                                detail.setVenteProduit(saved);
                                detail.setProduit(produit);
                                detail.setDate(saved.getDate());
                                detail.setQuantite(quantite);
                                venteDetailsService.save(detail);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    // Ignorer les entrées invalides
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("success", "Vente enregistrée");
        return "redirect:/admin/vente-produits";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        VenteProduit vente = venteProduitService.findById(id).orElse(new VenteProduit());
        model.addAttribute("venteProduit", vente);
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        model.addAttribute("produits", produitService.findAll());
        
        // Récupérer les produits déjà associés avec leurs quantités
        List<VenteDetails> details = venteDetailsService.findByVenteProduitId(id);
        Map<Integer, Integer> produitQuantites = new HashMap<>();
        for (VenteDetails detail : details) {
            produitQuantites.put(detail.getProduit().getIdProduit(), 
                                detail.getQuantite() != null ? detail.getQuantite() : 1);
        }
        model.addAttribute("produitQuantites", produitQuantites);
        
        return "backoffice/vente-produits/form";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        venteProduitService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Vente supprimée");
        return "redirect:/admin/vente-produits";
    }
    
    @GetMapping("/{id}/details")
    public String details(@PathVariable Integer id, Model model) {
        VenteProduit vente = venteProduitService.findById(id).orElse(null);
        model.addAttribute("venteProduit", vente);
        model.addAttribute("details", venteDetailsService.findByVenteProduitId(id));
        return "backoffice/vente-produits/details";
    }
}

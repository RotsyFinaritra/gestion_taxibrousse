package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.PaiementDiffusion;
import com.itu.taxibrousse.entity.PaiementDetails;
import com.itu.taxibrousse.service.PaiementDiffusionService;
import com.itu.taxibrousse.service.PaiementDetailsService;
import com.itu.taxibrousse.service.DiffusionPublicitaireService;
import com.itu.taxibrousse.service.DiffusionPubDetailsService;
import com.itu.taxibrousse.service.SocieteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/admin/paiements-diffusion")
public class PaiementDiffusionAdminController {

    private final PaiementDiffusionService paiementDiffusionService;
    private final PaiementDetailsService paiementDetailsService;
    private final DiffusionPublicitaireService diffusionPublicitaireService;
    private final DiffusionPubDetailsService diffusionPubDetailsService;
    private final SocieteService societeService;

    public PaiementDiffusionAdminController(PaiementDiffusionService paiementDiffusionService,
                                          PaiementDetailsService paiementDetailsService,
                                          DiffusionPublicitaireService diffusionPublicitaireService,
                                          DiffusionPubDetailsService diffusionPubDetailsService,
                                          SocieteService societeService) {
        this.paiementDiffusionService = paiementDiffusionService;
        this.paiementDetailsService = paiementDetailsService;
        this.diffusionPublicitaireService = diffusionPublicitaireService;
        this.diffusionPubDetailsService = diffusionPubDetailsService;
        this.societeService = societeService;
    }

    @GetMapping
    public String list(@RequestParam(value = "societeId", required = false) Integer societeId,
                       @RequestParam(value = "dateDebut", required = false) String dateDebut,
                       @RequestParam(value = "dateFin", required = false) String dateFin,
                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
                       Model model) {

        LocalDate debut = null;
        LocalDate fin = null;
        if (dateDebut != null && !dateDebut.isEmpty() && dateFin != null && !dateFin.isEmpty()) {
            try {
                debut = LocalDate.parse(dateDebut);
                fin = LocalDate.parse(dateFin);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Dates invalides");
            }
        }

        // Version simplifiée temporaire
        org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest.of(page, size);
        List<com.itu.taxibrousse.entity.PaiementDiffusion> paiements = paiementDiffusionService.findAll();
        org.springframework.data.domain.Page<com.itu.taxibrousse.entity.PaiementDiffusion> pageResult = 
            new org.springframework.data.domain.PageImpl<>(paiements, pageRequest, paiements.size());

        model.addAttribute("paiements", pageResult.getContent());
        model.addAttribute("paiementsPage", pageResult);
        model.addAttribute("currentPage", pageResult.getNumber());
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("pageSize", pageResult.getSize());

        model.addAttribute("selectedSocieteId", societeId);
        model.addAttribute("dateDebut", dateDebut);
        model.addAttribute("dateFin", dateFin);

        model.addAttribute("societes", societeService.findAll());
        return "backoffice/paiements-diffusion/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("paiement", new PaiementDiffusion());
        model.addAttribute("diffusionsPublicitaires", diffusionPublicitaireService.findAll());
        return "backoffice/paiements-diffusion/form";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return paiementDiffusionService.findById(id)
                .map(paiement -> {
                    model.addAttribute("paiement", paiement);
                    var details = paiementDetailsService.findByPaiementDiffusionId(id);
                    model.addAttribute("paiementDetails", details);
                    
                    // Calcul du total des détails
                    java.math.BigDecimal totalDetails = details.stream()
                        .map(detail -> detail.getMontant() != null ? detail.getMontant() : java.math.BigDecimal.ZERO)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                    model.addAttribute("totalDetails", totalDetails);
                    
                    return "backoffice/paiements-diffusion/detail";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Paiement non trouvé");
                    return "redirect:/admin/paiements-diffusion";
                });
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return paiementDiffusionService.findById(id)
                .map(paiement -> {
                    model.addAttribute("paiement", paiement);
                    model.addAttribute("diffusionsPublicitaires", diffusionPublicitaireService.findAll());
                    var details = paiementDetailsService.findByPaiementDiffusionId(id);
                    model.addAttribute("paiementDetails", details);
                    return "backoffice/paiements-diffusion/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Paiement non trouvé");
                    return "redirect:/admin/paiements-diffusion";
                });
    }

    @PostMapping
    public String save(@ModelAttribute PaiementDiffusion paiement,
                      @RequestParam("dateStr") String dateStr,
                      @RequestParam("moisAnneeAPayerStr") String moisAnneeAPayerStr,
                      @RequestParam("idDiffusionPublicitaire") Integer idDiffusionPublicitaire,
                      RedirectAttributes redirectAttributes) {
        try {
            if (dateStr != null && !dateStr.isEmpty()) {
                paiement.setDate(LocalDate.parse(dateStr));
            }
            if (moisAnneeAPayerStr != null && !moisAnneeAPayerStr.isEmpty()) {
                paiement.setMoisAnneeAPayer(LocalDate.parse(moisAnneeAPayerStr));
            }
            
            // Find the selected diffusion publicitaire
            var diffusionPubOpt = diffusionPublicitaireService.findById(idDiffusionPublicitaire);
            if (diffusionPubOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Diffusion publicitaire introuvable");
                return "redirect:/admin/paiements-diffusion";
            }
            
            var diffusionPub = diffusionPubOpt.get();
            paiement.setDiffusionPublicitaire(diffusionPub);
            
            // Save the payment header
            var savedPaiement = paiementDiffusionService.save(paiement);
            
            // Get all details for this diffusion publicitaire
            var details = diffusionPubDetailsService.findByDiffusionPublicitaireId(idDiffusionPublicitaire);
            
            if (!details.isEmpty()) {
                // Calculate total amount of the bill (sum of all details' estimatedCa)
                java.math.BigDecimal totalFacture = details.stream()
                    .map(com.itu.taxibrousse.entity.DiffusionPubDetails::getEstimatedCa)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
                if (totalFacture.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    // Distribute payment proportionally
                    for (var detail : details) {
                        java.math.BigDecimal detailCa = detail.getEstimatedCa();
                        java.math.BigDecimal proportion = detailCa.divide(totalFacture, 10, java.math.RoundingMode.HALF_UP);
                        java.math.BigDecimal montantDetail = paiement.getMontant().multiply(proportion);
                        
                        // Create payment detail
                        PaiementDetails paiementDetail = new PaiementDetails();
                        paiementDetail.setPaiementDiffusion(savedPaiement);
                        paiementDetail.setDiffusionDetails(detail);
                        paiementDetail.setMontant(montantDetail);
                        paiementDetail.setDate(savedPaiement.getDate());
                        
                        paiementDetailsService.save(paiementDetail);
                    }
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Paiement enregistré avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/admin/paiements-diffusion";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            paiementDiffusionService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Paiement supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/paiements-diffusion";
    }
}
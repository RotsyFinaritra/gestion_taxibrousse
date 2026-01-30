package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.DiffusionPublicitaire;
import com.itu.taxibrousse.entity.DiffusionPubDetails;
import com.itu.taxibrousse.service.DiffusionPublicitaireService;
import com.itu.taxibrousse.service.DiffusionPubDetailsService;
import com.itu.taxibrousse.service.VideoPublicitaireService;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.SocieteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/diffusions-publicitaires")
public class DiffusionPublicitaireAdminController {

    private final DiffusionPublicitaireService diffusionPublicitaireService;
    private final DiffusionPubDetailsService diffusionPubDetailsService;
    private final SocieteService societeService;
    private final VideoPublicitaireService videoPublicitaireService;
    private final VoyageVehiculeService voyageVehiculeService;

    public DiffusionPublicitaireAdminController(DiffusionPublicitaireService diffusionPublicitaireService,
                                              DiffusionPubDetailsService diffusionPubDetailsService,
                                              SocieteService societeService,
                                              VideoPublicitaireService videoPublicitaireService,
                                              VoyageVehiculeService voyageVehiculeService) {
        this.diffusionPublicitaireService = diffusionPublicitaireService;
        this.diffusionPubDetailsService = diffusionPubDetailsService;
        this.societeService = societeService;
        this.videoPublicitaireService = videoPublicitaireService;
        this.voyageVehiculeService = voyageVehiculeService;
    }

    @GetMapping
    public String list(Model model) {
        // List diffusion publicitaire headers
        var headers = diffusionPublicitaireService.findAll();
        model.addAttribute("diffusions", headers);
        // Provide counts of details per header
        java.util.Map<Integer, Integer> detailsCount = new java.util.HashMap<>();
        for (var h : headers) {
            if (h != null && h.getIdDiffusionPublicitaire() != null) {
                var list = diffusionPubDetailsService.findByDiffusionPublicitaireId(h.getIdDiffusionPublicitaire());
                detailsCount.put(h.getIdDiffusionPublicitaire(), list != null ? list.size() : 0);
            }
        }
        model.addAttribute("detailsCount", detailsCount);
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        return "backoffice/diffusions-publicitaires/list";
    }
    
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("diffusion", new DiffusionPubDetails());
        model.addAttribute("videos", videoPublicitaireService.findAll());
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        return "backoffice/diffusions-publicitaires/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("diffusion") DiffusionPubDetails diffusion,
                       @RequestParam(value = "dateDiffusionStr", required = false) String dateDiffusionStr,
                       RedirectAttributes redirectAttributes) {
        // Parse date/time if provided (format yyyy-MM-ddTHH:mm from datetime-local)
        try {
            if (dateDiffusionStr != null && !dateDiffusionStr.isBlank()) {
                java.time.LocalDateTime dt = java.time.LocalDateTime.parse(dateDiffusionStr);
                diffusion.setDateDiffusion(dt);
            } else if (diffusion.getDateDiffusion() == null) {
                diffusion.setDateDiffusion(java.time.LocalDateTime.now());
            }

            // Ensure diffusionPublicitaire header exists (find by month/year and societe, or create)
            if (diffusion.getDiffusionPublicitaire() == null) {
                var video = diffusion.getVideoPublicitaire();
                com.itu.taxibrousse.entity.Societe initialSoc = (video != null ? video.getSociete() : null);
                com.itu.taxibrousse.entity.Societe chosenSoc;
                if (initialSoc != null) {
                    chosenSoc = initialSoc;
                } else {
                    var listes = societeService.findAll();
                    if (!listes.isEmpty()) {
                        chosenSoc = listes.get(0);
                    } else {
                        throw new IllegalStateException("Aucune société disponible pour associer la diffusion");
                    }
                }

                java.time.LocalDateTime dateTime = diffusion.getDateDiffusion();
                java.time.LocalDate startOfMonth;
                if (dateTime != null) {
                    java.time.LocalDate d = dateTime.toLocalDate();
                    startOfMonth = java.time.LocalDate.of(d.getYear(), d.getMonthValue(), 1);
                } else {
                    java.time.LocalDate now = java.time.LocalDate.now();
                    startOfMonth = java.time.LocalDate.of(now.getYear(), now.getMonthValue(), 1);
                }
                java.time.LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

                java.util.List<DiffusionPublicitaire> candidates = diffusionPublicitaireService.findByDateDebutBetween(startOfMonth, endOfMonth);
                DiffusionPublicitaire header = null;
                if (!candidates.isEmpty()) {
                    if (chosenSoc != null) {
                        header = candidates.stream()
                                .filter(h -> h.getSociete() != null && h.getSociete().getIdSociete() != null && h.getSociete().getIdSociete().equals(chosenSoc.getIdSociete()))
                                .findFirst()
                                .orElse(candidates.get(0));
                    } else {
                        header = candidates.get(0);
                    }
                }

                if (header == null) {
                    header = new DiffusionPublicitaire();
                    header.setDateDebut(startOfMonth);
                    header.setSociete(chosenSoc);
                    diffusionPublicitaireService.save(header);
                }

                diffusion.setDiffusionPublicitaire(header);
            }

            diffusionPubDetailsService.save(diffusion);
            redirectAttributes.addFlashAttribute("successMessage", "Diffusion enregistrée avec succès");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + ex.getMessage());
        }

        return "redirect:/admin/diffusions-publicitaires";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        return diffusionPubDetailsService.findById(id)
            .map(detail -> {
                model.addAttribute("diffusion", detail);
                model.addAttribute("videos", videoPublicitaireService.findAll());
                model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
                return "backoffice/diffusions-publicitaires/form";
            })
            .orElse("redirect:/admin/diffusions-publicitaires");
    }
    
    @GetMapping("/{id}")
    public String viewHeaderDetails(@PathVariable Integer id, Model model) {
        var opt = diffusionPublicitaireService.findById(id);
        if (opt.isPresent()) {
            var header = opt.get();
            model.addAttribute("diffusion", header);
            var details = diffusionPubDetailsService.findByDiffusionPublicitaireId(id);
            model.addAttribute("details", details);
            return "backoffice/diffusions-publicitaires/details-list";
        }
        return "redirect:/admin/diffusions-publicitaires";
    }

    @GetMapping("/detail/{id}")
    public String viewDetailById(@PathVariable Integer id, Model model) {
        var opt = diffusionPubDetailsService.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("diffusion", opt.get());
            return "backoffice/diffusions-publicitaires/view";
        }
        return "redirect:/admin/diffusions-publicitaires";
    }
    
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id,
                         @ModelAttribute("diffusion") DiffusionPubDetails diffusion,
                         @RequestParam(value = "dateDiffusionStr", required = false) String dateDiffusionStr,
                         RedirectAttributes redirectAttributes) {
        try {
            if (dateDiffusionStr != null && !dateDiffusionStr.isBlank()) {
                diffusion.setDateDiffusion(java.time.LocalDateTime.parse(dateDiffusionStr));
            }
            diffusion.setIdDiffusionPubDetails(id);
            diffusionPubDetailsService.save(diffusion);
            redirectAttributes.addFlashAttribute("successMessage", "Diffusion mise à jour");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour: " + ex.getMessage());
        }
        return "redirect:/admin/diffusions-publicitaires";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            diffusionPubDetailsService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Diffusion supprimée");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression: " + ex.getMessage());
        }
        return "redirect:/admin/diffusions-publicitaires";
    }
    
    @PostMapping("/{id}/cloturer")
    public String cloturer(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        diffusionPublicitaireService.cloturerDiffusion(id, LocalDate.now());
        redirectAttributes.addFlashAttribute("success", "Diffusion clôturée avec succès");
        return "redirect:/admin/diffusions-publicitaires";
    }
    
    @GetMapping("/statistiques")
    public String statistiques(@RequestParam(value = "mois", required = false) Integer mois,
                               @RequestParam(value = "annee", required = false) Integer annee,
                               Model model) {
        
        if (mois == null) {
            mois = LocalDate.now().getMonthValue();
        }
        if (annee == null) {
            annee = LocalDate.now().getYear();
        }
        
        // Récupérer tous les détails et filtrer sur la période demandée
        var details = diffusionPubDetailsService.findAll();

        // Filtrer par mois/année
        final int selectedMois = mois;
        final int selectedAnnee = annee;
        var periodDetails = details.stream()
            .filter(d -> d.getDateDiffusion() != null && d.getDateDiffusion().getMonthValue() == selectedMois && d.getDateDiffusion().getYear() == selectedAnnee)
            .toList();

        // Calculs
        long nombreEnregistrements = periodDetails.size();
        long totalDiffusions = periodDetails.stream()
            .map(d -> d.getNbDiffusion() != null ? d.getNbDiffusion() : 0)
            .mapToLong(Integer::longValue)
            .sum();

        BigDecimal chiffreAffaires = periodDetails.stream()
            .filter(d -> d.getVideoPublicitaire() != null && d.getVideoPublicitaire().getSociete() != null)
            .map(d -> {
                BigDecimal tarif = d.getVideoPublicitaire().getSociete().getTarifParDiffusion();
                Integer nb = d.getNbDiffusion() != null ? d.getNbDiffusion() : 0;
                return tarif != null ? tarif.multiply(BigDecimal.valueOf(nb)) : BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Créer un objet statistiques pour le template
        StatistiquesDto statistiques = new StatistiquesDto();
        statistiques.nombreEnregistrements = nombreEnregistrements;
        statistiques.totalDiffusions = totalDiffusions;
        statistiques.chiffreAffaires = chiffreAffaires;

        model.addAttribute("statistiques", statistiques);
        model.addAttribute("mois", mois);
        model.addAttribute("annee", annee);
        model.addAttribute("societes", societeService.findAll());
        
        return "backoffice/diffusions-publicitaires/statistiques";
    }
    
    @PostMapping("/details/{id}/delete")
    public String deleteDetail(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            var detailOpt = diffusionPubDetailsService.findById(id);
            if (detailOpt.isPresent()) {
                var detail = detailOpt.get();
                var diffusionId = detail.getDiffusionPublicitaire() != null ? detail.getDiffusionPublicitaire().getIdDiffusionPublicitaire() : null;
                
                diffusionPubDetailsService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Détail de diffusion supprimé avec succès");
                
                if (diffusionId != null) {
                    return "redirect:/admin/diffusions-publicitaires/" + diffusionId;
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Détail de diffusion non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/diffusions-publicitaires";
    }
    
    // Classe DTO pour les statistiques
    public static class StatistiquesDto {
        public Long nombreEnregistrements;
        public Long totalDiffusions;
        public java.math.BigDecimal chiffreAffaires;
        
        public Long getNombreEnregistrements() { return nombreEnregistrements; }
        public Long getTotalDiffusions() { return totalDiffusions; }
        public java.math.BigDecimal getChiffreAffaires() { return chiffreAffaires; }
    }
}
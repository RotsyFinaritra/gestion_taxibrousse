package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.VideoPublicitaire;
import com.itu.taxibrousse.service.VideoPublicitaireService;
import com.itu.taxibrousse.service.SocieteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/videos-publicitaires")
public class VideoPublicitaireAdminController {

    private final VideoPublicitaireService videoPublicitaireService;
    private final SocieteService societeService;

    public VideoPublicitaireAdminController(VideoPublicitaireService videoPublicitaireService, 
                                          SocieteService societeService) {
        this.videoPublicitaireService = videoPublicitaireService;
        this.societeService = societeService;
    }

    @GetMapping
    public String list(@RequestParam(value = "societeId", required = false) Integer societeId, Model model) {
        if (societeId != null) {
            model.addAttribute("videos", videoPublicitaireService.findBySocieteId(societeId));
            model.addAttribute("selectedSocieteId", societeId);
        } else {
            model.addAttribute("videos", videoPublicitaireService.findAllOrderByDateAjoutDesc());
        }
        model.addAttribute("societes", societeService.findAllOrderByNom());
        return "backoffice/videos-publicitaires/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("video", new VideoPublicitaire());
        model.addAttribute("societes", societeService.findAllOrderByNom());
        return "backoffice/videos-publicitaires/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute VideoPublicitaire video, RedirectAttributes ra) {
        try {
            if (video.getDateAjout() == null) {
                video.setDateAjout(LocalDate.now());
            }
            videoPublicitaireService.save(video);
            ra.addFlashAttribute("successMessage", "Vidéo publicitaire créée avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la création de la vidéo : " + e.getMessage());
        }
        return "redirect:/admin/videos-publicitaires";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            VideoPublicitaire video = videoPublicitaireService.findById(id).orElseThrow(() -> 
                new RuntimeException("Vidéo publicitaire non trouvée avec l'ID : " + id));
            model.addAttribute("video", video);
            model.addAttribute("societes", societeService.findAllOrderByNom());
            return "backoffice/videos-publicitaires/form";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Vidéo non trouvée : " + e.getMessage());
            return "redirect:/admin/videos-publicitaires";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute VideoPublicitaire video, RedirectAttributes ra) {
        try {
            videoPublicitaireService.update(video);
            ra.addFlashAttribute("successMessage", "Vidéo publicitaire mise à jour avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la mise à jour : " + e.getMessage());
        }
        return "redirect:/admin/videos-publicitaires";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            if (!videoPublicitaireService.existsById(id)) {
                throw new RuntimeException("Vidéo publicitaire non trouvée avec l'ID : " + id);
            }
            videoPublicitaireService.deleteById(id);
            ra.addFlashAttribute("successMessage", "Vidéo publicitaire supprimée avec succès !");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/admin/videos-publicitaires";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            VideoPublicitaire video = videoPublicitaireService.findById(id).orElseThrow(() -> 
                new RuntimeException("Vidéo publicitaire non trouvée avec l'ID : " + id));
            model.addAttribute("video", video);
            return "backoffice/videos-publicitaires/view";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Vidéo non trouvée : " + e.getMessage());
            return "redirect:/admin/videos-publicitaires";
        }
    }

    @GetMapping("/search")
    public String search(@RequestParam("titre") String titre, Model model) {
        model.addAttribute("videos", videoPublicitaireService.findByTitreContaining(titre));
        model.addAttribute("societes", societeService.findAllOrderByNom());
        model.addAttribute("searchTitre", titre);
        return "backoffice/videos-publicitaires/list";
    }
}
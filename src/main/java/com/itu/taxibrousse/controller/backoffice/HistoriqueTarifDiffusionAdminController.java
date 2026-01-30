package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.service.HistoriqueTarifDiffusionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/historique-tarif-diffusion")
public class HistoriqueTarifDiffusionAdminController {

    private final HistoriqueTarifDiffusionService historiqueService;

    public HistoriqueTarifDiffusionAdminController(HistoriqueTarifDiffusionService historiqueService) {
        this.historiqueService = historiqueService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("historiques", historiqueService.findAll());
        return "backoffice/historique-tarif-diffusion/list";
    }
}

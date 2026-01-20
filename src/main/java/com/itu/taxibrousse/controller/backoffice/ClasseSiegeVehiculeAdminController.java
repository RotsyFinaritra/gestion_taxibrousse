package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.ClasseSiegeVehicule;
import com.itu.taxibrousse.entity.Vehicule;
import com.itu.taxibrousse.entity.Classe;
import com.itu.taxibrousse.service.ClasseSiegeVehiculeService;
import com.itu.taxibrousse.service.VehiculeService;
import com.itu.taxibrousse.service.ClasseService;
import com.itu.taxibrousse.service.SeatLayoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
@RequestMapping("/admin/classe-siege-vehicule")
public class ClasseSiegeVehiculeAdminController {

    private final ClasseSiegeVehiculeService csService;
    private final VehiculeService vehiculeService;
    private final ClasseService classeService;
    private final SeatLayoutService seatLayoutService;
    
    private static final Logger logger = LoggerFactory.getLogger(ClasseSiegeVehiculeAdminController.class);

    public ClasseSiegeVehiculeAdminController(ClasseSiegeVehiculeService csService,
                                              VehiculeService vehiculeService,
                                              ClasseService classeService,
                                              SeatLayoutService seatLayoutService) {
        this.csService = csService;
        this.vehiculeService = vehiculeService;
        this.classeService = classeService;
        this.seatLayoutService = seatLayoutService;
    }

    @GetMapping
    public String list(Model model) {
        List<ClasseSiegeVehicule> all = csService.findAll();
        model.addAttribute("entries", all);
        return "backoffice/classe_siege_vehicule/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(value = "vehiculeId", required = false) Integer vehiculeId,
                          Model model) {
        model.addAttribute("vehicules", vehiculeService.findAll());
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("selectedVehiculeId", vehiculeId);

        String seatLayoutHtml = null;
        int capacity = 0;
        if (vehiculeId != null) {
            Vehicule v = vehiculeService.findById(vehiculeId).orElse(null);
            if (v != null) {
                capacity = v.getCapacite() != null ? v.getCapacite() : 0;
                // compute already linked seats for this vehicle
                java.util.List<com.itu.taxibrousse.entity.ClasseSiegeVehicule> mapped = csService.findByVehicule(vehiculeId);
                java.util.List<Integer> linked = new java.util.ArrayList<>();
                java.util.Map<Integer,Integer> seatToClass = new java.util.HashMap<>();
                for (com.itu.taxibrousse.entity.ClasseSiegeVehicule m : mapped) {
                    if (m.getNumPlace() != null) {
                        linked.add(m.getNumPlace());
                        if (m.getClasse() != null && m.getClasse().getIdClasse() != null) seatToClass.put(m.getNumPlace(), m.getClasse().getIdClasse());
                    }
                }
                seatLayoutHtml = seatLayoutService.generateSeatLayoutHtml(capacity, linked, seatToClass);
            }
        }
        model.addAttribute("seatLayoutHtml", seatLayoutHtml);
        model.addAttribute("capacity", capacity);
        return "backoffice/classe_siege_vehicule/form";
    }

    @GetMapping(value = "/seat-layout", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> seatLayout(@RequestParam("vehiculeId") Integer vehiculeId) {
        logger.info("seatLayout called for vehiculeId={}", vehiculeId);
        Vehicule v = vehiculeService.findById(vehiculeId).orElse(null);
        if (v == null) {
            logger.info("No vehicule found for id={}", vehiculeId);
            return ResponseEntity.ok("");
        }
        int capacity = v.getCapacite() != null ? v.getCapacite() : 0;
        java.util.List<com.itu.taxibrousse.entity.ClasseSiegeVehicule> mapped = csService.findByVehicule(vehiculeId);
        java.util.List<Integer> linked = new java.util.ArrayList<>();
        java.util.Map<Integer,Integer> seatToClass = new java.util.HashMap<>();
        for (com.itu.taxibrousse.entity.ClasseSiegeVehicule m : mapped) {
            if (m.getNumPlace() != null) {
                linked.add(m.getNumPlace());
                if (m.getClasse() != null && m.getClasse().getIdClasse() != null) seatToClass.put(m.getNumPlace(), m.getClasse().getIdClasse());
            }
        }
        String html = seatLayoutService.generateSeatLayoutHtml(capacity, linked, seatToClass);
        logger.info("Returning seat layout HTML length={} for vehiculeId={}", html != null ? html.length() : 0, vehiculeId);
        return ResponseEntity.ok(html);
    }

    @PostMapping
    public String save(@RequestParam(value = "vehiculeId", required = true) Integer vehiculeId,
                       @RequestParam(value = "selectedSeatsByClass", required = false) String selectedSeatsByClass,
                       RedirectAttributes redirectAttributes) {
        try {
            // remove existing mappings for this vehicle
            List<ClasseSiegeVehicule> existing = csService.findByVehicule(vehiculeId);
            for (ClasseSiegeVehicule e : existing) csService.deleteById(e.getIdClasseSiegeVehicule());

            if (selectedSeatsByClass != null && !selectedSeatsByClass.trim().isEmpty()) {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                java.util.Map<String, Object> parsed = mapper.readValue(selectedSeatsByClass, java.util.Map.class);
                for (Object entryObj : parsed.entrySet()) {
                    java.util.Map.Entry entry = (java.util.Map.Entry) entryObj;
                    String classKey = String.valueOf(entry.getKey());
                    Integer classId = null;
                    try { classId = Integer.valueOf(classKey); } catch (NumberFormatException ignore) {}
                    java.util.List seats = (java.util.List) entry.getValue();
                    if (seats == null || classId == null) continue;
                    Classe cls = classeService.findById(classId).orElse(null);
                    for (Object seatObj : seats) {
                        try {
                            Integer seatNum = Integer.valueOf(String.valueOf(seatObj));
                            ClasseSiegeVehicule cs = new ClasseSiegeVehicule();
                            cs.setVehicule(vehiculeService.findById(vehiculeId).orElse(null));
                            cs.setClasse(cls);
                            cs.setNumPlace(seatNum);
                            csService.save(cs);
                        } catch (NumberFormatException ignore) {}
                    }
                }
            }
            

            redirectAttributes.addFlashAttribute("success", "Mappings enregistrés avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
        return "redirect:/admin/classe-siege-vehicule";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            csService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Suppression réussie");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
        return "redirect:/admin/classe-siege-vehicule";
    }
}

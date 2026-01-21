package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Reservation;
import com.itu.taxibrousse.service.ReservationService;
import com.itu.taxibrousse.service.ClientService;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.SeatLayoutService;
import com.itu.taxibrousse.service.ReservationDetailsService;
import com.itu.taxibrousse.service.CategorieService;
import com.itu.taxibrousse.service.ClasseCategorieService;
import com.itu.taxibrousse.service.ClasseService;
import com.itu.taxibrousse.service.ClasseSiegeVehiculeService;
import com.itu.taxibrousse.entity.VoyageVehicule;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Controller
@RequestMapping("/admin/reservations")
public class ReservationAdminController {
    
    private final ReservationService reservationService;
    private final ClientService clientService;
    private final VoyageVehiculeService voyageVehiculeService;
    private final SeatLayoutService seatLayoutService;
    private final ReservationDetailsService reservationDetailsService;
    private final CategorieService categorieService;
    private final ClasseCategorieService classeCategorieService;
    private final ClasseService classeService;
    private final ClasseSiegeVehiculeService csService;
    private static final Logger logger = LoggerFactory.getLogger(ReservationAdminController.class);

    public ReservationAdminController(ReservationService reservationService,
                                      ClientService clientService,
                                      VoyageVehiculeService voyageVehiculeService,
                                      SeatLayoutService seatLayoutService,
                                      ReservationDetailsService reservationDetailsService,
                                      CategorieService categorieService,
                                      ClasseCategorieService classeCategorieService,
                                      ClasseService classeService,
                                      ClasseSiegeVehiculeService csService) {
        this.reservationService = reservationService;
        this.clientService = clientService;
        this.voyageVehiculeService = voyageVehiculeService;
        this.seatLayoutService = seatLayoutService;
        this.reservationDetailsService = reservationDetailsService;
        this.categorieService = categorieService;
        this.classeCategorieService = classeCategorieService;
        this.classeService = classeService;
        this.csService = csService;
    }
    
    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "backoffice/reservations/list";
    }
    
    @GetMapping("/new")
    public String newForm(@RequestParam(value = "voyageVehiculeId", required = false) Integer voyageVehiculeId,
                         @RequestParam(value = "selectedSeats", required = false) String selectedSeats,
                         @RequestParam(value = "nom", required = false) String nom,
                         @RequestParam(value = "contact", required = false) String contact,
                         @RequestParam(value = "clientId", required = false) Integer clientId,
                         @RequestParam(value = "categorieId", required = false) Integer categorieId,
                         @RequestParam(value = "etat", required = false) Integer etat,
                         @RequestParam(value = "date", required = false) String dateParam,
                         Model model) {
        Reservation reservation = new Reservation();
        // Prefill fields from request params so values persist on page refresh
        if (nom != null) reservation.setNom(nom);
        if (contact != null) reservation.setContact(contact);
        if (clientId != null) clientService.findById(clientId).ifPresent(reservation::setClient);
        if (etat != null) reservation.setEtat(etat);
        if (dateParam != null && !dateParam.isBlank()) {
            try {
                reservation.setDate(LocalDateTime.parse(dateParam));
            } catch (DateTimeParseException ex) {
                // try with seconds if user agent omitted them
                try {
                    reservation.setDate(LocalDateTime.parse(dateParam + ":00"));
                } catch (DateTimeParseException ex2) {
                    reservation.setDate(LocalDateTime.now());
                }
            }
        } else {
            reservation.setDate(LocalDateTime.now());
        }
        model.addAttribute("reservation", reservation);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("selectedCategorieId", categorieId);
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        model.addAttribute("classes", classeService.findAll());
        model.addAttribute("classeCategories", classeCategorieService.findAll());

        // Si un voyage est sélectionné, générer le plan de sièges et récupérer les sièges déjà réservés
        String seatLayoutHtml = null;
        int capacity = 0;
        if (voyageVehiculeId != null) {
            VoyageVehicule vv = voyageVehiculeService.findById(voyageVehiculeId).orElse(null);
            if (vv != null && vv.getVehicule() != null) {
                capacity = vv.getVehicule().getCapacite() != null ? vv.getVehicule().getCapacite() : 0;
                java.util.List<Integer> reservedSeats = new java.util.ArrayList<>();
                java.util.List<com.itu.taxibrousse.entity.Reservation> existing = reservationService.findByVoyageVehicule(voyageVehiculeId);
                for (com.itu.taxibrousse.entity.Reservation r : existing) {
                    java.util.List<com.itu.taxibrousse.entity.ReservationDetails> details = reservationDetailsService.findByReservation(r.getIdReservation());
                    for (com.itu.taxibrousse.entity.ReservationDetails d : details) {
                        if (d.getNumPlace() != null) reservedSeats.add(d.getNumPlace());
                    }
                }
                // Préremplir le voyageVehicule sélectionné pour que le select affiche la valeur
                reservation.setVoyageVehicule(vv);
                // fetch seat->class mapping for this vehicle so seat blocks can be colored
                java.util.Map<Integer,Integer> seatToClass = new java.util.HashMap<>();
                try {
                    java.util.List<com.itu.taxibrousse.entity.ClasseSiegeVehicule> mapped = csService.findByVehicule(vv.getVehicule().getIdVehicule());
                    for (com.itu.taxibrousse.entity.ClasseSiegeVehicule m : mapped) {
                        if (m.getNumPlace() != null && m.getClasse() != null && m.getClasse().getIdClasse() != null) {
                            seatToClass.put(m.getNumPlace(), m.getClasse().getIdClasse());
                        }
                    }
                } catch (Exception ignore) {}
                seatLayoutHtml = seatLayoutService.generateSeatLayoutHtml(capacity, reservedSeats, seatToClass);
            }
        }
        model.addAttribute("seatLayoutHtml", seatLayoutHtml);
        model.addAttribute("capacity", capacity);
        model.addAttribute("selectedSeats", selectedSeats);
        // Etats (HTML badges) for the select; computed server-side to avoid static access in Thymeleaf
        java.util.Map<Integer, String> etatOptions = new java.util.HashMap<>();
        etatOptions.put(0, Reservation.getEtatLibelle(0));
        etatOptions.put(10, Reservation.getEtatLibelle(10));
        etatOptions.put(1, Reservation.getEtatLibelle(1));
        etatOptions.put(-1, Reservation.getEtatLibelle(-1));
        model.addAttribute("etatOptions", etatOptions);
        return "backoffice/reservations/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return reservationService.findById(id)
            .map(reservation -> {
                model.addAttribute("reservation", reservation);
                model.addAttribute("clients", clientService.findAll());
                model.addAttribute("categories", categorieService.findAll());
                model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
                model.addAttribute("classes", classeService.findAll());
                model.addAttribute("classeCategories", classeCategorieService.findAll());
                // Provide etat options as HTML badges
                java.util.Map<Integer, String> etatOptions = new java.util.HashMap<>();
                etatOptions.put(1, Reservation.getEtatLibelle(1));
                etatOptions.put(10, Reservation.getEtatLibelle(10));
                etatOptions.put(0, Reservation.getEtatLibelle(0));
                etatOptions.put(-1, Reservation.getEtatLibelle(-1));
                model.addAttribute("etatOptions", etatOptions);
                // Generate seat layout with reserved seats for the voyage, excluding this reservation's own seats
                String seatLayoutHtml = null;
                int capacity = 0;
                if (reservation.getVoyageVehicule() != null) {
                    Integer vvId = reservation.getVoyageVehicule().getIdVoyageVehicule();
                    VoyageVehicule vv = voyageVehiculeService.findById(vvId).orElse(null);
                        if (vv != null && vv.getVehicule() != null) {
                        capacity = vv.getVehicule().getCapacite() != null ? vv.getVehicule().getCapacite() : 0;
                        java.util.Set<Integer> reservedSeats = new java.util.HashSet<>();
                        java.util.List<com.itu.taxibrousse.entity.Reservation> existing = reservationService.findByVoyageVehicule(vvId);
                        for (com.itu.taxibrousse.entity.Reservation r : existing) {
                            if (r.getIdReservation().equals(reservation.getIdReservation())) continue; // exclude current
                            java.util.List<com.itu.taxibrousse.entity.ReservationDetails> details = reservationDetailsService.findByReservation(r.getIdReservation());
                            for (com.itu.taxibrousse.entity.ReservationDetails d : details) {
                                if (d.getNumPlace() != null) reservedSeats.add(d.getNumPlace());
                            }
                        }
                        // include seat->class mapping so colors appear for classes
                        java.util.Map<Integer,Integer> seatToClass = new java.util.HashMap<>();
                        try {
                            java.util.List<com.itu.taxibrousse.entity.ClasseSiegeVehicule> mapped = csService.findByVehicule(vv.getVehicule().getIdVehicule());
                            for (com.itu.taxibrousse.entity.ClasseSiegeVehicule m : mapped) {
                                if (m.getNumPlace() != null && m.getClasse() != null && m.getClasse().getIdClasse() != null) {
                                    seatToClass.put(m.getNumPlace(), m.getClasse().getIdClasse());
                                }
                            }
                        } catch (Exception ignore) {}
                        seatLayoutHtml = seatLayoutService.generateSeatLayoutHtml(capacity, new java.util.ArrayList<>(reservedSeats), seatToClass);
                    }
                }
                model.addAttribute("seatLayoutHtml", seatLayoutHtml);
                model.addAttribute("capacity", capacity);
                // prefill selectedSeats from reservation details
                java.util.List<com.itu.taxibrousse.entity.ReservationDetails> currentDetails = new java.util.ArrayList<>();
                if (reservation.getIdReservation() != null) {
                    currentDetails = reservationDetailsService.findByReservation(reservation.getIdReservation());
                }
                StringBuilder sel = new StringBuilder();
                for (com.itu.taxibrousse.entity.ReservationDetails d : currentDetails) {
                    if (d.getNumPlace() != null) {
                        if (sel.length() > 0) sel.append(',');
                        sel.append(d.getNumPlace());
                    }
                }
                model.addAttribute("selectedSeats", sel.toString());
                return "backoffice/reservations/form";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Réservation non trouvée");
                return "redirect:/admin/reservations";
            });
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        return reservationService.findById(id)
            .map(reservation -> {
                model.addAttribute("reservation", reservation);
                java.util.List<com.itu.taxibrousse.entity.ReservationDetails> details = reservationDetailsService.findByReservation(reservation.getIdReservation());
                // compute per-detail montant using ReservationDetailsService helper
                if (details != null) {
                    for (com.itu.taxibrousse.entity.ReservationDetails d : details) {
                        try {
                            if (d.getMontant() == null) {
                                java.math.BigDecimal calc = reservationDetailsService.getMontantCalculated(d, reservation.getVoyageVehicule());
                                if (calc != null) d.setMontant(calc.doubleValue());
                            }
                        } catch (Exception ignore) {}
                    }
                }
                model.addAttribute("details", details);
                java.math.BigDecimal total = reservationService.getMontantTotal(reservation);
                model.addAttribute("total", total.setScale(2, java.math.RoundingMode.HALF_UP));
                return "backoffice/reservations/view";
            })
            .orElseGet(() -> {
                redirectAttributes.addFlashAttribute("error", "Réservation non trouvée");
                return "redirect:/admin/reservations";
            });
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @PostMapping
    public String save(@ModelAttribute Reservation reservation,
                       @RequestParam(value = "selectedSeats", required = false) String selectedSeats,
                       @RequestParam(value = "selectedSeatsByCategory", required = false) String selectedSeatsByCategory,
                       @RequestParam(value = "selectedSeatsByClasseCategorie", required = false) String selectedSeatsByClasseCategorie,
                       @RequestParam(value = "montantParCategorie", required = false) String montantParCategorie,
                       @RequestParam(value = "nombrePlace", required = false) Integer nombrePlace,
                       RedirectAttributes redirectAttributes) {
        try {
            // ensure nombrePlace is set on reservation
            if (nombrePlace != null) reservation.setNombrePlace(nombrePlace);

            // save reservation first
            Reservation saved = reservationService.save(reservation);

            // remove existing details for this reservation
            java.util.List<com.itu.taxibrousse.entity.ReservationDetails> existing = reservationDetailsService.findByReservation(saved.getIdReservation());
            for (com.itu.taxibrousse.entity.ReservationDetails d : existing) {
                reservationDetailsService.deleteById(d.getIdReservationDetails());
            }

            // create details from mapping if provided, otherwise fallback to simple selectedSeats
            // Prefer server-friendly mapping: seat -> idClasseCategorie
            java.util.Map<String, Double> montantParCategorieMap = null;
            if (montantParCategorie != null && !montantParCategorie.trim().isEmpty()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    montantParCategorieMap = mapper.readValue(montantParCategorie, java.util.Map.class);
                } catch (Exception ex) {
                    logger.warn("Could not parse montantParCategorie: {}", ex.getMessage());
                }
            }

            if (selectedSeatsByClasseCategorie != null && !selectedSeatsByClasseCategorie.trim().isEmpty()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    java.util.Map<String, Object> parsed = mapper.readValue(selectedSeatsByClasseCategorie, java.util.Map.class);
                    for (java.util.Map.Entry<String, Object> e : parsed.entrySet()) {
                        try {
                            Integer seatNum = Integer.valueOf(e.getKey());
                            Integer idClasseCategorie = null;
                            Object v = e.getValue();
                            if (v != null) {
                                try { idClasseCategorie = Integer.valueOf(String.valueOf(v)); } catch (NumberFormatException ignore) {}
                            }
                            com.itu.taxibrousse.entity.ReservationDetails rd = new com.itu.taxibrousse.entity.ReservationDetails();
                            rd.setNumPlace(seatNum);
                            com.itu.taxibrousse.entity.ClasseCategorie chosenCC = null;
                            if (idClasseCategorie != null) chosenCC = classeCategorieService.findById(idClasseCategorie).orElse(null);
                            // If possible, use montant from montantParCategorie for the category label
                            Double montant = null;
                            if (chosenCC != null && chosenCC.getCategorie() != null && montantParCategorieMap != null) {
                                String catLabel = chosenCC.getCategorie().getLibelle();
                                if (catLabel != null && montantParCategorieMap.containsKey(catLabel)) {
                                    try { montant = Double.valueOf(String.valueOf(montantParCategorieMap.get(catLabel))); } catch (Exception ignore) {}
                                }
                            }
                            if (montant == null) {
                                java.math.BigDecimal price = reservationService.computePriceForSeat(chosenCC, null, saved.getVoyageVehicule());
                                montant = price != null ? price.doubleValue() : null;
                            }
                            rd.setMontant(montant);
                            rd.setEtat(1);
                            rd.setReservation(saved);
                            if (chosenCC != null) rd.setClasseCategorie(chosenCC);
                            reservationDetailsService.save(rd);
                        } catch (NumberFormatException ex) {
                            // ignore invalid seat key
                        }
                    }
                } catch (Exception ex) {
                    // parsing failed, fallback to existing behaviors below
                }
            } else if (selectedSeatsByCategory != null && !selectedSeatsByCategory.trim().isEmpty()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    java.util.Map<String, Object> parsed = mapper.readValue(selectedSeatsByCategory, java.util.Map.class);
                    // detect if value is list (category -> [seats]) or single number (seat -> cat)
                    boolean parsedAsCatToList = false;
                    if (!parsed.isEmpty()) {
                        Object firstVal = parsed.values().iterator().next();
                        parsedAsCatToList = (firstVal instanceof java.util.List);
                    }
                    if (parsedAsCatToList) {
                        // expected format: {"2":[1,5], "1":[3]}
                        for (Object entryObj : parsed.entrySet()) {
                            java.util.Map.Entry entry = (java.util.Map.Entry) entryObj;
                            String catKey = String.valueOf(entry.getKey());
                            Integer parsedCatId = null;
                            try { parsedCatId = Integer.valueOf(catKey); } catch (NumberFormatException ignore) {}
                            final Integer catId = parsedCatId;
                            java.util.List seats = (java.util.List) entry.getValue();
                            if (seats == null) continue;
                            for (Object seatObj : seats) {
                                try {
                                    Integer seatNum = Integer.valueOf(String.valueOf(seatObj));
                                    com.itu.taxibrousse.entity.ReservationDetails rd = new com.itu.taxibrousse.entity.ReservationDetails();
                                    rd.setNumPlace(seatNum);
                                        com.itu.taxibrousse.entity.ClasseCategorie chosenCC = null;
                                        if (catId != null) {
                                        // try to resolve ClasseCategorie using seat->classe mapping for this vehicle
                                        Integer vehiculeId = saved.getVoyageVehicule() != null && saved.getVoyageVehicule().getVehicule() != null
                                            ? saved.getVoyageVehicule().getVehicule().getIdVehicule() : null;
                                        com.itu.taxibrousse.entity.ClasseCategorie found = null;
                                        if (vehiculeId != null) {
                                            java.util.Optional<com.itu.taxibrousse.entity.ClasseSiegeVehicule> seatMapOpt = csService.findByVehiculeAndNumPlace(vehiculeId, seatNum);
                                            com.itu.taxibrousse.entity.ClasseSiegeVehicule seatMap = seatMapOpt.orElse(null);
                                            if (seatMap != null && seatMap.getClasse() != null && seatMap.getClasse().getIdClasse() != null) {
                                                final Integer expectedClasseId = seatMap.getClasse().getIdClasse();
                                                found = classeCategorieService.findByClasseAndCategorie(expectedClasseId, catId).orElse(null);
                                            }
                                        }
                                        if (found == null) {
                                            // fallback: any ClasseCategorie for this categorie
                                            java.util.List<com.itu.taxibrousse.entity.ClasseCategorie> list = classeCategorieService.findByCategorie(catId);
                                            if (!list.isEmpty()) found = list.get(0);
                                        }
                                        chosenCC = found;
                                        }
                                    // compute price using ClasseCategorie only (do not persist a separate Categorie on ReservationDetails)
                                    // If possible, use montant from montantParCategorie for the category label
                                    Double montant = null;
                                    if (chosenCC != null && chosenCC.getCategorie() != null && montantParCategorieMap != null) {
                                        String catLabel = chosenCC.getCategorie().getLibelle();
                                        if (catLabel != null && montantParCategorieMap.containsKey(catLabel)) {
                                            try { montant = Double.valueOf(String.valueOf(montantParCategorieMap.get(catLabel))); } catch (Exception ignore) {}
                                        }
                                    }
                                    if (montant == null) {
                                        java.math.BigDecimal price = reservationService.computePriceForSeat(chosenCC, null, saved.getVoyageVehicule());
                                        montant = price != null ? price.doubleValue() : null;
                                    }
                                    rd.setMontant(montant);
                                    rd.setEtat(1);
                                    rd.setReservation(saved);
                                    if (chosenCC != null) {
                                        if (chosenCC.getIdClasseCategorie() != null) {
                                            chosenCC = classeCategorieService.findById(chosenCC.getIdClasseCategorie()).orElse(chosenCC);
                                        }
                                        logger.debug("Assigning ClasseCategorie id={} categorieId={}",
                                                chosenCC.getIdClasseCategorie(),
                                                chosenCC.getCategorie() != null ? chosenCC.getCategorie().getIdCategorie() : null);
                                        rd.setClasseCategorie(chosenCC);
                                    }
                                    reservationDetailsService.save(rd);
                                } catch (NumberFormatException ex) {
                                    // ignore invalid seat value
                                }
                            }
                        }
                    } else {
                        // fallback: seat -> cat mapping {"1":2, "3":1}
                        java.util.Map<String, Object> map = parsed;
                        for (java.util.Map.Entry<String, Object> e : map.entrySet()) {
                            try {
                                Integer seatNum = Integer.valueOf(e.getKey());
                                Integer parsedCatId = null;
                                Object v = e.getValue();
                                if (v != null) {
                                    try { parsedCatId = Integer.valueOf(String.valueOf(v)); } catch (NumberFormatException ignore) {}
                                }
                                final Integer catId = parsedCatId;
                                com.itu.taxibrousse.entity.ReservationDetails rd = new com.itu.taxibrousse.entity.ReservationDetails();
                                rd.setNumPlace(seatNum);
                                com.itu.taxibrousse.entity.ClasseCategorie chosenCC = null;
                                if (catId != null) {
                                    Integer vehiculeId = saved.getVoyageVehicule() != null && saved.getVoyageVehicule().getVehicule() != null
                                        ? saved.getVoyageVehicule().getVehicule().getIdVehicule() : null;
                                    com.itu.taxibrousse.entity.ClasseCategorie found = null;
                                    if (vehiculeId != null) {
                                    java.util.Optional<com.itu.taxibrousse.entity.ClasseSiegeVehicule> seatMapOpt = csService.findByVehiculeAndNumPlace(vehiculeId, seatNum);
                                    com.itu.taxibrousse.entity.ClasseSiegeVehicule seatMap = seatMapOpt.orElse(null);
                                        if (seatMap != null && seatMap.getClasse() != null && seatMap.getClasse().getIdClasse() != null) {
                                            final Integer expectedClasseId = seatMap.getClasse().getIdClasse();
                                            found = classeCategorieService.findByClasseAndCategorie(expectedClasseId, catId).orElse(null);
                                        }
                                    }
                                    if (found == null) {
                                        java.util.List<com.itu.taxibrousse.entity.ClasseCategorie> list = classeCategorieService.findByCategorie(catId);
                                        if (!list.isEmpty()) found = list.get(0);
                                    }
                                    chosenCC = found;
                                }
                                // compute price using ClasseCategorie only (do not persist a separate Categorie on ReservationDetails)
                                // If possible, use montant from montantParCategorie for the category label
                                Double montant = null;
                                if (chosenCC != null && chosenCC.getCategorie() != null && montantParCategorieMap != null) {
                                    String catLabel = chosenCC.getCategorie().getLibelle();
                                    if (catLabel != null && montantParCategorieMap.containsKey(catLabel)) {
                                        try { montant = Double.valueOf(String.valueOf(montantParCategorieMap.get(catLabel))); } catch (Exception ignore) {}
                                    }
                                }
                                if (montant == null) {
                                    java.math.BigDecimal price = reservationService.computePriceForSeat(chosenCC, null, saved.getVoyageVehicule());
                                    montant = price != null ? price.doubleValue() : null;
                                }
                                rd.setMontant(montant);
                                rd.setEtat(1);
                                rd.setReservation(saved);
                                if (chosenCC != null) {
                                    if (chosenCC.getIdClasseCategorie() != null) {
                                        chosenCC = classeCategorieService.findById(chosenCC.getIdClasseCategorie()).orElse(chosenCC);
                                    }
                                    logger.debug("Assigning ClasseCategorie id={} categorieId={}",
                                            chosenCC.getIdClasseCategorie(),
                                            chosenCC.getCategorie() != null ? chosenCC.getCategorie().getIdCategorie() : null);
                                    rd.setClasseCategorie(chosenCC);
                                }
                                reservationDetailsService.save(rd);
                            } catch (NumberFormatException ex) {
                                // ignore invalid seat key
                            }
                        }
                    }
                } catch (Exception ex) {
                    // parsing failed, fallback to simple behavior below
                }
            } else if (selectedSeats != null && !selectedSeats.trim().isEmpty()) {
                String[] parts = selectedSeats.split(",");
                for (String p : parts) {
                    String s = p.trim();
                    if (s.isEmpty()) continue;
                    try {
                        Integer seatNum = Integer.valueOf(s);
                        com.itu.taxibrousse.entity.ReservationDetails rd = new com.itu.taxibrousse.entity.ReservationDetails();
                        rd.setNumPlace(seatNum);
                        // no category assigned -> compute with null ClasseCategorie and null Categorie
                        java.math.BigDecimal price = reservationService.computePriceForSeat(null, null, saved.getVoyageVehicule());
                        rd.setMontant(price != null ? price.doubleValue() : null);
                        rd.setEtat(1);
                        rd.setReservation(saved);
                        reservationDetailsService.save(rd);
                    } catch (NumberFormatException ex) {
                        // ignore invalid seat value
                    }
                }
            }

            redirectAttributes.addFlashAttribute("success", "Réservation enregistrée avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement de la réservation", e);
            String msg = e.getMessage() != null ? e.getMessage() : e.toString();
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement de la réservation: " + msg);
        }
        return "redirect:/admin/reservations";
    }
    
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Réservation supprimée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression de la réservation");
        }
        return "redirect:/admin/reservations";
    }

    /**
     * AJAX endpoint: get computed price for a voyageVehicule given optional classeId and categorieId.
     * Returns JSON: { "price": "12.00", "source": "tarif_classe_trajet" }
     */
    @GetMapping(value = "/price", produces = "application/json")
    @ResponseBody
    public java.util.Map<String, Object> getPrice(@RequestParam("voyageVehiculeId") Integer voyageVehiculeId,
                                                  @RequestParam(value = "classeId", required = false) Integer classeId,
                                                  @RequestParam(value = "categorieId", required = false) Integer categorieId) {
        java.util.Map<String, Object> resp = new java.util.HashMap<>();
        try {
            com.itu.taxibrousse.entity.VoyageVehicule vv = voyageVehiculeService.findById(voyageVehiculeId).orElse(null);
            com.itu.taxibrousse.entity.Categorie cat = null;
            if (categorieId != null) cat = categorieService.findById(categorieId).orElse(null);

            // Return tariffs for all classes/categories for the selected voyageVehicule (ignore form selections)
            java.util.List<com.itu.taxibrousse.service.ReservationService.PriceResult> options =
                    reservationService.collectAllOptionsForVoyageVehicule(vv);
            // return options as array of {price, source}
            java.util.List<java.util.Map<String, String>> opts = new java.util.ArrayList<>();
            for (com.itu.taxibrousse.service.ReservationService.PriceResult p : options) {
                java.util.Map<String, String> m = new java.util.HashMap<>();
                m.put("price", p.price != null ? p.price.setScale(2, java.math.RoundingMode.HALF_UP).toString() : "0.00");
                m.put("source", p.source != null ? p.source : "");
                m.put("label", p.label != null ? p.label : "");
                opts.add(m);
            }
            resp.put("options", opts);
            // keep a convenience "price" and "source" (first option) for backward compatibility
            if (!opts.isEmpty()) {
                resp.put("price", opts.get(0).get("price"));
                resp.put("source", opts.get(0).get("source"));
            } else {
                resp.put("price", "0.00");
                resp.put("source", "none");
            }
            resp.put("success", true);
        } catch (Exception ex) {
            resp.put("success", false);
            resp.put("error", ex.getMessage());
            resp.put("price", "0.00");
        }
        return resp;
    }
}

package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.VenteDetails;
import com.itu.taxibrousse.entity.VenteProduit;
import com.itu.taxibrousse.service.VoyageVehiculeService;
import com.itu.taxibrousse.service.ReservationService;
import com.itu.taxibrousse.service.DiffusionPublicitaireService;
import com.itu.taxibrousse.service.DiffusionPubDetailsService;
import com.itu.taxibrousse.service.PaiementDetailsService;
import com.itu.taxibrousse.service.VenteProduitService;
import com.itu.taxibrousse.service.HistoriquePrixProduitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reports/chiffre-affaire-voyage-vehicule")
public class ChiffreAffaireVoyageVehiculeAdminController {
    
    private final VoyageVehiculeService voyageVehiculeService;
    private final ReservationService reservationService;
    private final DiffusionPublicitaireService diffusionPublicitaireService;
    private final DiffusionPubDetailsService diffusionPubDetailsService;
    private final PaiementDetailsService paiementDetailsService;
    private final VenteProduitService venteProduitService;
    private final HistoriquePrixProduitService historiquePrixProduitService;
    
    public ChiffreAffaireVoyageVehiculeAdminController(VoyageVehiculeService voyageVehiculeService,
                                                       ReservationService reservationService,
                                                       DiffusionPublicitaireService diffusionPublicitaireService,
                                                       DiffusionPubDetailsService diffusionPubDetailsService,
                                                       PaiementDetailsService paiementDetailsService,
                                                       VenteProduitService venteProduitService,
                                                       HistoriquePrixProduitService historiquePrixProduitService) {
        this.voyageVehiculeService = voyageVehiculeService;
        this.reservationService = reservationService;
        this.diffusionPublicitaireService = diffusionPublicitaireService;
        this.diffusionPubDetailsService = diffusionPubDetailsService;
        this.paiementDetailsService = paiementDetailsService;
        this.venteProduitService = venteProduitService;
        this.historiquePrixProduitService = historiquePrixProduitService;
    }
    
    @GetMapping
    public String afficherChiffreAffaire(Model model,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "vehicleId", required = false) Integer vehicleId,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "immatriculation", required = false) String immatriculation,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "dateDebut", required = false) String dateDebutStr,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "dateFin", required = false) String dateFinStr,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "heure", required = false) String heure,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "sortBy", required = false) String sortBy,
                                        @org.springframework.web.bind.annotation.RequestParam(value = "sortDir", required = false) String sortDir) {

        List<ChiffreAffaireVoyageVehiculeDto> chiffreAffaires = voyageVehiculeService.findAll()
            .stream()
            .map(this::calculerChiffreAffaire)
            .collect(Collectors.toList());

        // Apply filters
        if (vehicleId != null) {
            chiffreAffaires = chiffreAffaires.stream()
                .filter(c -> c.getIdVoyageVehicule() != null && c.getIdVoyageVehicule().equals(vehicleId))
                .collect(Collectors.toList());
        }
        if (immatriculation != null && !immatriculation.isBlank()) {
            String immLower = immatriculation.trim().toLowerCase();
            chiffreAffaires = chiffreAffaires.stream()
                .filter(c -> c.getVehicule() != null && c.getVehicule().getImmatriculation() != null && c.getVehicule().getImmatriculation().toLowerCase().contains(immLower))
                .collect(Collectors.toList());
        }
        // Filtre par date début
        if (dateDebutStr != null && !dateDebutStr.isBlank()) {
            try {
                LocalDate dateDebut = LocalDate.parse(dateDebutStr);
                chiffreAffaires = chiffreAffaires.stream()
                    .filter(c -> c.getDateDepart() != null && !c.getDateDepart().isBefore(dateDebut))
                    .collect(Collectors.toList());
            } catch (Exception ex) {
                // ignore parse errors
            }
        }
        // Filtre par date fin
        if (dateFinStr != null && !dateFinStr.isBlank()) {
            try {
                LocalDate dateFin = LocalDate.parse(dateFinStr);
                chiffreAffaires = chiffreAffaires.stream()
                    .filter(c -> c.getDateDepart() != null && !c.getDateDepart().isAfter(dateFin))
                    .collect(Collectors.toList());
            } catch (Exception ex) {
                // ignore parse errors
            }
        }
        if (heure != null && !heure.isBlank()) {
            String heureTrim = heure.trim();
            chiffreAffaires = chiffreAffaires.stream()
                .filter(c -> c.getHeureDepart() != null && c.getHeureDepart().contains(heureTrim))
                .collect(Collectors.toList());
        }

        // Sorting
        if (sortBy != null) {
            java.util.Comparator<ChiffreAffaireVoyageVehiculeDto> comparator = null;
            if ("tickets".equals(sortBy)) {
                comparator = java.util.Comparator.comparing(c -> c.getMontantTickets() != null ? c.getMontantTickets() : BigDecimal.ZERO);
            } else if ("publicite".equals(sortBy)) {
                comparator = java.util.Comparator.comparing(c -> c.getMontantPublicite() != null ? c.getMontantPublicite() : BigDecimal.ZERO);
            } else if ("venteProduits".equals(sortBy)) {
                comparator = java.util.Comparator.comparing(c -> c.getMontantVenteProduits() != null ? c.getMontantVenteProduits() : BigDecimal.ZERO);
            } else if ("total".equals(sortBy)) {
                comparator = java.util.Comparator.comparing(c -> c.getMontantTotal() != null ? c.getMontantTotal() : BigDecimal.ZERO);
            }
            if (comparator != null) {
                if ("desc".equalsIgnoreCase(sortDir)) {
                    comparator = comparator.reversed();
                }
                chiffreAffaires = chiffreAffaires.stream().sorted(comparator).collect(Collectors.toList());
            }
        }
        // Totaux globaux calculés côté serveur to avoid complex SpEL in templates
        java.math.BigDecimal totalTickets = chiffreAffaires.stream()
            .map(ChiffreAffaireVoyageVehiculeDto::getMontantTickets)
            .filter(Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalPublicite = chiffreAffaires.stream()
            .map(ChiffreAffaireVoyageVehiculeDto::getMontantPublicite)
            .filter(Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalVenteProduits = chiffreAffaires.stream()
            .map(ChiffreAffaireVoyageVehiculeDto::getMontantVenteProduits)
            .filter(Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        java.math.BigDecimal totalGeneral = chiffreAffaires.stream()
            .map(ChiffreAffaireVoyageVehiculeDto::getMontantTotal)
            .filter(Objects::nonNull)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("chiffreAffaires", chiffreAffaires);
        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("totalPublicite", totalPublicite);
        model.addAttribute("totalVenteProduits", totalVenteProduits);
        model.addAttribute("totalGeneral", totalGeneral);
        // Add filter/sort context for template
        model.addAttribute("voyageVehicules", voyageVehiculeService.findAll());
        model.addAttribute("selectedVehicleId", vehicleId);
        model.addAttribute("selectedImmatriculation", immatriculation);
        model.addAttribute("selectedDateDebut", dateDebutStr);
        model.addAttribute("selectedDateFin", dateFinStr);
        model.addAttribute("selectedHeure", heure);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        return "backoffice/reports/chiffre-affaire-voyage-vehicule";
    }
    
    private ChiffreAffaireVoyageVehiculeDto calculerChiffreAffaire(com.itu.taxibrousse.entity.VoyageVehicule vv) {
        ChiffreAffaireVoyageVehiculeDto dto = new ChiffreAffaireVoyageVehiculeDto();
        
        // Informations de base
        dto.setIdVoyageVehicule(vv.getIdVoyageVehicule());
        dto.setVehicule(vv.getVehicule());
        dto.setDateDepart(vv.getVoyage() != null ? vv.getVoyage().getDateDepart() : null);
        dto.setHeureDepart(((vv.getVoyage() != null && vv.getVoyage().getHeureDepart() != null) ? vv.getVoyage().getHeureDepart().getHeure().toString() : null));
        
        if (vv.getVoyage() != null && vv.getVoyage().getTrajet() != null) {
            dto.setGareRoutiereDepart(vv.getVoyage().getTrajet().getGareRoutiereDepart());
            dto.setGareRoutiereArrivee(vv.getVoyage().getTrajet().getGareRoutiereArrivee());
        }
        
        // Calcul des montants
        List<com.itu.taxibrousse.entity.Reservation> reservations = reservationService.findByVoyageVehicule(vv.getIdVoyageVehicule());
        BigDecimal montantTickets = reservations.stream()
            .map(reservationService::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<com.itu.taxibrousse.entity.DiffusionPubDetails> diffusionDetails = diffusionPubDetailsService.findByVoyageVehiculeId(vv.getIdVoyageVehicule());
        BigDecimal montantPublicite = diffusionDetails.stream()
            .filter(d -> d.getVideoPublicitaire() != null && d.getVideoPublicitaire().getSociete() != null)
            .map(d -> {
                BigDecimal tarif = d.getVideoPublicitaire().getSociete().getTarifParDiffusion();
                Integer nbDiffusions = d.getNbDiffusion();
                return tarif.multiply(BigDecimal.valueOf(nbDiffusions != null ? nbDiffusions : 0));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcul des montants payés pour les diffusions
        BigDecimal montantPayeDiffusions = diffusionDetails.stream()
            .map(detail -> {
                var paiements = paiementDetailsService.findByDiffusionDetailsId(detail.getIdDiffusionPubDetails());
                return paiements.stream()
                    .map(p -> p.getMontant() != null ? p.getMontant() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal resteAPayerDiffusions = montantPublicite.subtract(montantPayeDiffusions);
        
        // Calcul du CA Vente Produits pour ce voyage véhicule
        List<VenteProduit> venteProduits = venteProduitService.findByVoyageVehiculeId(vv.getIdVoyageVehicule());
        BigDecimal montantVenteProduits = BigDecimal.ZERO;
        for (VenteProduit vp : venteProduits) {
            List<VenteDetails> details = vp.getVenteDetails();
            if (details != null) {
                for (VenteDetails vd : details) {
                    if (vd.getProduit() != null && vd.getProduit().getIdProduit() != null) {
                        LocalDate dateVente = vd.getDate() != null ? vd.getDate() : (vp.getDate() != null ? vp.getDate() : LocalDate.now());
                        BigDecimal prixUnitaire = historiquePrixProduitService
                                .getPrixAtDate(vd.getProduit().getIdProduit(), dateVente)
                                .orElse(vd.getProduit().getPrix());
                        if (prixUnitaire != null) {
                            Integer quantite = vd.getQuantite() != null ? vd.getQuantite() : 1;
                            montantVenteProduits = montantVenteProduits.add(prixUnitaire.multiply(BigDecimal.valueOf(quantite)));
                        }
                    }
                }
            }
        }
        
        dto.setMontantTickets(montantTickets);
        dto.setMontantPublicite(montantPublicite);
        dto.setMontantVenteProduits(montantVenteProduits);
        dto.setMontantTotal(montantTickets.add(montantPublicite).add(montantVenteProduits));
        dto.setMontantPayeDiffusions(montantPayeDiffusions);
        dto.setResteAPayerDiffusions(resteAPayerDiffusions);
        
        return dto;
    }
    
    public static class ChiffreAffaireVoyageVehiculeDto {
        private Integer idVoyageVehicule;
        private com.itu.taxibrousse.entity.GareRoutiere gareRoutiereDepart;
        private com.itu.taxibrousse.entity.GareRoutiere gareRoutiereArrivee;
        private com.itu.taxibrousse.entity.Vehicule vehicule;
        private LocalDate dateDepart;
        private String heureDepart;
        private BigDecimal montantTickets;
        private BigDecimal montantPublicite;
        private BigDecimal montantVenteProduits;
        private BigDecimal montantTotal;
        private BigDecimal montantPayeDiffusions;
        private BigDecimal resteAPayerDiffusions;
        
        // Getters and setters
        public Integer getIdVoyageVehicule() { return idVoyageVehicule; }
        public void setIdVoyageVehicule(Integer idVoyageVehicule) { this.idVoyageVehicule = idVoyageVehicule; }
        
        public com.itu.taxibrousse.entity.GareRoutiere getGareRoutiereDepart() { return gareRoutiereDepart; }
        public void setGareRoutiereDepart(com.itu.taxibrousse.entity.GareRoutiere gareRoutiereDepart) { this.gareRoutiereDepart = gareRoutiereDepart; }
        
        public com.itu.taxibrousse.entity.GareRoutiere getGareRoutiereArrivee() { return gareRoutiereArrivee; }
        public void setGareRoutiereArrivee(com.itu.taxibrousse.entity.GareRoutiere gareRoutiereArrivee) { this.gareRoutiereArrivee = gareRoutiereArrivee; }
        
        public com.itu.taxibrousse.entity.Vehicule getVehicule() { return vehicule; }
        public void setVehicule(com.itu.taxibrousse.entity.Vehicule vehicule) { this.vehicule = vehicule; }
        
        public LocalDate getDateDepart() { return dateDepart; }
        public void setDateDepart(LocalDate dateDepart) { this.dateDepart = dateDepart; }
        
        public String getHeureDepart() { return heureDepart; }
        public void setHeureDepart(String heureDepart) { this.heureDepart = heureDepart; }
        
        public BigDecimal getMontantTickets() { return montantTickets; }
        public void setMontantTickets(BigDecimal montantTickets) { this.montantTickets = montantTickets; }
        
        public BigDecimal getMontantPublicite() { return montantPublicite; }
        public void setMontantPublicite(BigDecimal montantPublicite) { this.montantPublicite = montantPublicite; }
        
        public BigDecimal getMontantVenteProduits() { return montantVenteProduits; }
        public void setMontantVenteProduits(BigDecimal montantVenteProduits) { this.montantVenteProduits = montantVenteProduits; }
        
        public BigDecimal getMontantTotal() { return montantTotal; }
        public void setMontantTotal(BigDecimal montantTotal) { this.montantTotal = montantTotal; }
        
        public BigDecimal getMontantPayeDiffusions() { return montantPayeDiffusions; }
        public void setMontantPayeDiffusions(BigDecimal montantPayeDiffusions) { this.montantPayeDiffusions = montantPayeDiffusions; }
        
        public BigDecimal getResteAPayerDiffusions() { return resteAPayerDiffusions; }
        public void setResteAPayerDiffusions(BigDecimal resteAPayerDiffusions) { this.resteAPayerDiffusions = resteAPayerDiffusions; }
    }
}

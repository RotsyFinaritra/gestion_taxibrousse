package com.itu.taxibrousse.controller.backoffice;

import com.itu.taxibrousse.entity.Produit;
import com.itu.taxibrousse.entity.VenteDetails;
import com.itu.taxibrousse.service.DiffusionPubDetailsService;
import com.itu.taxibrousse.service.HistoriquePrixProduitService;
import com.itu.taxibrousse.service.ReservationService;
import com.itu.taxibrousse.service.VenteDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reports/ca-global")
public class CAGlobalAdminController {

    private final ReservationService reservationService;
    private final DiffusionPubDetailsService diffusionPubDetailsService;
    private final VenteDetailsService venteDetailsService;
    private final HistoriquePrixProduitService historiquePrixProduitService;

    public CAGlobalAdminController(ReservationService reservationService,
                                   DiffusionPubDetailsService diffusionPubDetailsService,
                                   VenteDetailsService venteDetailsService,
                                   HistoriquePrixProduitService historiquePrixProduitService) {
        this.reservationService = reservationService;
        this.diffusionPubDetailsService = diffusionPubDetailsService;
        this.venteDetailsService = venteDetailsService;
        this.historiquePrixProduitService = historiquePrixProduitService;
    }

    @GetMapping
    public String showCAGlobalPage(Model model) {
        LocalDate now = LocalDate.now();
        model.addAttribute("currentMonth", now.getMonthValue());
        model.addAttribute("currentYear", now.getYear());
        return "backoffice/reports/ca-global";
    }

    @GetMapping("/compute")
    @ResponseBody
    public Map<String, Object> computeCA(@RequestParam("mois") Integer mois,
                                         @RequestParam("annee") Integer annee) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. CA Tickets (reservations)
            BigDecimal caTickets = reservationService.calculateRevenueByMonth(mois, annee);
            if (caTickets == null) {
                caTickets = BigDecimal.ZERO;
            }
            caTickets = caTickets.setScale(2, RoundingMode.HALF_UP);

            // 2. CA Diffusion Publicitaire
            BigDecimal caDiffusionPub = diffusionPubDetailsService.calculateRevenueByMonth(mois, annee);
            if (caDiffusionPub == null) {
                caDiffusionPub = BigDecimal.ZERO;
            }
            caDiffusionPub = caDiffusionPub.setScale(2, RoundingMode.HALF_UP);

            // 3. CA Vente Produits
            BigDecimal caVenteProduits = calculateVenteProduitsRevenue(mois, annee);
            caVenteProduits = caVenteProduits.setScale(2, RoundingMode.HALF_UP);

            // 4. Total Global
            BigDecimal totalGlobal = caTickets.add(caDiffusionPub).add(caVenteProduits);
            totalGlobal = totalGlobal.setScale(2, RoundingMode.HALF_UP);

            result.put("success", true);
            result.put("caTickets", caTickets);
            result.put("caDiffusionPub", caDiffusionPub);
            result.put("caVenteProduits", caVenteProduits);
            result.put("totalGlobal", totalGlobal);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("caTickets", BigDecimal.ZERO);
            result.put("caDiffusionPub", BigDecimal.ZERO);
            result.put("caVenteProduits", BigDecimal.ZERO);
            result.put("totalGlobal", BigDecimal.ZERO);
        }

        return result;
    }

    @GetMapping("/details-produits")
    @ResponseBody
    public Map<String, Object> getDetailsProduits(@RequestParam("mois") Integer mois,
                                                   @RequestParam("annee") Integer annee) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            YearMonth yearMonth = YearMonth.of(annee, mois);
            LocalDate dateDebut = yearMonth.atDay(1);
            LocalDate dateFin = yearMonth.atEndOfMonth();

            List<VenteDetails> venteDetails = venteDetailsService.findByDateBetween(dateDebut, dateFin);

            // Grouper par produit
            Map<Integer, List<VenteDetails>> parProduit = venteDetails.stream()
                .filter(vd -> vd.getProduit() != null && vd.getProduit().getIdProduit() != null)
                .collect(Collectors.groupingBy(vd -> vd.getProduit().getIdProduit()));

            List<Map<String, Object>> produits = new ArrayList<>();
            BigDecimal totalCA = BigDecimal.ZERO;

            for (Map.Entry<Integer, List<VenteDetails>> entry : parProduit.entrySet()) {
                List<VenteDetails> ventes = entry.getValue();
                Produit produit = ventes.get(0).getProduit();
                
                int quantiteTotale = 0;
                BigDecimal caTotalProduit = BigDecimal.ZERO;
                BigDecimal sommePrix = BigDecimal.ZERO;
                int nombreVentes = 0;

                for (VenteDetails vd : ventes) {
                    LocalDate dateVente = vd.getDate() != null ? vd.getDate() : dateDebut;
                    
                    BigDecimal prixUnitaire = historiquePrixProduitService
                            .getPrixAtDate(produit.getIdProduit(), dateVente)
                            .orElse(produit.getPrix());
                    
                    if (prixUnitaire != null) {
                        Integer quantite = vd.getQuantite() != null ? vd.getQuantite() : 1;
                        quantiteTotale += quantite;
                        BigDecimal ca = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
                        caTotalProduit = caTotalProduit.add(ca);
                        sommePrix = sommePrix.add(prixUnitaire);
                        nombreVentes++;
                    }
                }

                BigDecimal prixMoyen = nombreVentes > 0 
                    ? sommePrix.divide(BigDecimal.valueOf(nombreVentes), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

                Map<String, Object> produitInfo = new HashMap<>();
                produitInfo.put("nomProduit", produit.getNom());
                produitInfo.put("quantite", quantiteTotale);
                produitInfo.put("prixMoyen", prixMoyen);
                produitInfo.put("caTotal", caTotalProduit);
                
                produits.add(produitInfo);
                totalCA = totalCA.add(caTotalProduit);
            }

            // Trier par CA décroissant
            produits.sort((p1, p2) -> ((BigDecimal) p2.get("caTotal")).compareTo((BigDecimal) p1.get("caTotal")));

            result.put("success", true);
            result.put("produits", produits);
            result.put("totalCA", totalCA);

        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("produits", new ArrayList<>());
            result.put("totalCA", BigDecimal.ZERO);
        }

        return result;
    }

    /**
     * Calculate revenue from product sales for a given month/year.
     * Uses the historical price of each product at the sale date.
     */
    private BigDecimal calculateVenteProduitsRevenue(Integer mois, Integer annee) {
        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDate dateDebut = yearMonth.atDay(1);
        LocalDate dateFin = yearMonth.atEndOfMonth();

        List<VenteDetails> venteDetails = venteDetailsService.findByDateBetween(dateDebut, dateFin);

        BigDecimal total = BigDecimal.ZERO;
        for (VenteDetails vd : venteDetails) {
            if (vd.getProduit() != null && vd.getProduit().getIdProduit() != null) {
                LocalDate dateVente = vd.getDate();
                if (dateVente == null) {
                    dateVente = dateDebut; // fallback to start of month
                }
                
                // Get the price at the sale date from history
                BigDecimal prixUnitaire = historiquePrixProduitService
                        .getPrixAtDate(vd.getProduit().getIdProduit(), dateVente)
                        .orElse(vd.getProduit().getPrix()); // fallback to current price
                
                if (prixUnitaire != null) {
                    Integer quantite = vd.getQuantite() != null ? vd.getQuantite() : 1;
                    total = total.add(prixUnitaire.multiply(BigDecimal.valueOf(quantite)));
                }
            }
        }

        return total;
    }
}

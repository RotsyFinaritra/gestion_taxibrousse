package com.itu.taxibrousse.service;

import com.itu.taxibrousse.entity.Reservation;
import com.itu.taxibrousse.repository.ReservationRepository;
import com.itu.taxibrousse.entity.ClasseCategorie;
import com.itu.taxibrousse.entity.Categorie;
import com.itu.taxibrousse.entity.TarifClasseCategorieTrajet;
import com.itu.taxibrousse.entity.TarifClasseTrajet;
import com.itu.taxibrousse.entity.TarifSpecialCategorie;
import com.itu.taxibrousse.entity.VoyageVehicule;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final TarifSpecialCategorieService tarifSpecialCategorieService;
    private final TarifClasseCategorieTrajetService tarifClasseCategorieTrajetService;
    private final TarifClasseTrajetService tarifClasseTrajetService;
    private final VoyageVehiculeService voyageVehiculeService;
    private final ReservationDetailsService reservationDetailsService;

    public ReservationService(ReservationRepository reservationRepository,
            TarifSpecialCategorieService tarifSpecialCategorieService,
            TarifClasseCategorieTrajetService tarifClasseCategorieTrajetService,
            TarifClasseTrajetService tarifClasseTrajetService,
            VoyageVehiculeService voyageVehiculeService,
            ReservationDetailsService reservationDetailsService) {
        this.reservationRepository = reservationRepository;
        this.tarifSpecialCategorieService = tarifSpecialCategorieService;
        this.tarifClasseCategorieTrajetService = tarifClasseCategorieTrajetService;
        this.tarifClasseTrajetService = tarifClasseTrajetService;
        this.voyageVehiculeService = voyageVehiculeService;
        this.reservationDetailsService = reservationDetailsService;
    }

    /**
     * Compute the price for a single seat according to rules in
     * regle_de_gestions.md.
     * Priority:
     * 1) tarif_special_categorie (remise % applied to base tarif from
     * tarif_classe_categorie_trajet)
     * 2) tarif_classe_categorie_trajet (montant)
     * 3) tarif_classe_trajet (montant)
     * 4) voyageVehicule.prix -> voyage.prix -> trajet.prix
     *
     * @param classeCategorie ClasseCategorie (contains classe + categorie)
     * @param categorie       optional selected categorie (can be null)
     * @param voyageVehicule  the VoyageVehicule selected
     * @return price as BigDecimal (scale 2)
     */
    public BigDecimal computePriceForSeat(ClasseCategorie classeCategorie, Categorie categorie,
            VoyageVehicule voyageVehicule) {
        PriceResult res = computePriceForSeatWithSource(classeCategorie, categorie, voyageVehicule);
        return res.price;
    }

    /**
     * Compute price and indicate which rule/source was used.
     */
    public static class PriceResult {
        public final BigDecimal price;
        public final String source;
        public final String label;
        public PriceResult(BigDecimal price, String source) {
            this(price, source, null);
        }

        public PriceResult(BigDecimal price, String source, String label) {
            this.price = price;
            this.source = source;
            this.label = label;
        }
    }

    public PriceResult computePriceForSeatWithSource(ClasseCategorie classeCategorie, Categorie categorie,
            VoyageVehicule voyageVehicule) {
        if (voyageVehicule == null)
            return new PriceResult(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), "none");
        final LocalDate voyageDate = (voyageVehicule.getVoyage() != null) ? voyageVehicule.getVoyage().getDateDepart()
                : null;

        // 1) tarif_special_categorie -> look for specials matching classeCategorie+categorie
        if (classeCategorie != null && classeCategorie.getIdClasseCategorie() != null && categorie != null && categorie.getIdCategorie() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            List<TarifClasseCategorieTrajet> bases = tarifClasseCategorieTrajetService.findByTrajetIdAndDate(trajetId, voyageDate);
            List<TarifSpecialCategorie> specials = tarifSpecialCategorieService.findByClasseAndCategorie(classeCategorie.getClasse().getIdClasse(), categorie.getIdCategorie());
            for (TarifSpecialCategorie tsc : specials) {
                java.math.BigDecimal price = tarifSpecialCategorieService.computeSpecialPrice(tsc, bases, null);
                if (price != null) return new PriceResult(price, "tarif_special_categorie");
            }
        }

        // 2) tarif_classe_categorie_trajet
        if (classeCategorie != null && classeCategorie.getIdClasseCategorie() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            for (TarifClasseCategorieTrajet tcc : tarifClasseCategorieTrajetService.findByTrajetIdAndDate(trajetId, voyageDate)) {
                if (tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getIdClasseCategorie() != null
                        && tcc.getClasseCategorie().getIdClasseCategorie().equals(classeCategorie.getIdClasseCategorie())
                        && tcc.getMontant() != null) {
                    return new PriceResult(tcc.getMontant().setScale(2, RoundingMode.HALF_UP), "tarif_classe_categorie_trajet");
                }
            }
        }

        // 3) tarif_classe_trajet (by classe + trajet + date)
        if (classeCategorie != null && classeCategorie.getClasse() != null && classeCategorie.getClasse().getIdClasse() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            for (TarifClasseTrajet tct : tarifClasseTrajetService.findByTrajetIdAndDate(trajetId, voyageDate)) {
                if (tct.getClasse() != null && tct.getClasse().getIdClasse() != null
                        && tct.getClasse().getIdClasse().equals(classeCategorie.getClasse().getIdClasse())
                        && tct.getMontant() != null) {
                    return new PriceResult(tct.getMontant().setScale(2, RoundingMode.HALF_UP), "tarif_classe_trajet");
                }
            }
        }

        // 4) voyageVehicule.prix -> voyage.prix -> trajet.prix
        if (voyageVehicule.getPrix() != null)
            return new PriceResult(BigDecimal.valueOf(voyageVehicule.getPrix()).setScale(2, RoundingMode.HALF_UP),
                    "voyageVehicule.prix");
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getPrix() != null)
            return new PriceResult(
                    BigDecimal.valueOf(voyageVehicule.getVoyage().getPrix()).setScale(2, RoundingMode.HALF_UP),
                    "voyage.prix");
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null
                && voyageVehicule.getVoyage().getTrajet().getPrix() != null)
            return new PriceResult(BigDecimal.valueOf(voyageVehicule.getVoyage().getTrajet().getPrix()).setScale(2,
                    RoundingMode.HALF_UP), "trajet.prix");

        return new PriceResult(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), "none");
    }

    /**
     * Compute the total montant for a reservation by summing computed seat prices
     * for each ReservationDetails using the same precedence rules.
     */
    public java.math.BigDecimal getMontantTotal(Reservation reservation) {
        if (reservation == null || reservation.getIdReservation() == null) {
            return java.math.BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP);
        }
        java.util.List<com.itu.taxibrousse.entity.ReservationDetails> details = reservationDetailsService.findByReservation(reservation.getIdReservation());
        if (details == null || details.isEmpty()) {
            return java.math.BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP);
        }
        java.math.BigDecimal total = java.math.BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP);
        VoyageVehicule vv = reservation.getVoyageVehicule();
        for (com.itu.taxibrousse.entity.ReservationDetails rd : details) {
            try {
                com.itu.taxibrousse.entity.ClasseCategorie cc = rd.getClasseCategorie();
                com.itu.taxibrousse.entity.Categorie cat = cc != null ? cc.getCategorie() : null;
                // use computePriceForSeatWithSource to respect precedence (specials, classe+categorie, classe, base)
                PriceResult pr = computePriceForSeatWithSource(cc, cat, vv);
                if (pr != null && pr.price != null) {
                    total = total.add(pr.price.setScale(2, java.math.RoundingMode.HALF_UP));
                } else if (rd.getMontant() != null) {
                    total = total.add(java.math.BigDecimal.valueOf(rd.getMontant()).setScale(2, java.math.RoundingMode.HALF_UP));
                }
            } catch (Exception e) {
                // fallback: if ReservationDetails has montant, add it
                if (rd.getMontant() != null) {
                    total = total.add(java.math.BigDecimal.valueOf(rd.getMontant()).setScale(2, java.math.RoundingMode.HALF_UP));
                }
            }
        }
        return total.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Collect all applicable price options for the given parameters.
     * Returns a list ordered by precedence (specials first, then classe+categorie, classe, then voyage/voyageVehicule/trajet bases).
     */
    public List<PriceResult> collectPriceOptions(ClasseCategorie classeCategorie, Categorie categorie,
            VoyageVehicule voyageVehicule) {
        List<PriceResult> out = new java.util.ArrayList<>();
        if (voyageVehicule == null) return out;
        final java.time.LocalDate voyageDate = (voyageVehicule.getVoyage() != null) ? voyageVehicule.getVoyage().getDateDepart() : null;

        // 1) tarif_special_categorie -> compute discounted amounts when base tarif_classe_categorie_trajet exists
        if (classeCategorie != null && classeCategorie.getIdClasseCategorie() != null && categorie != null && categorie.getIdCategorie() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            List<TarifClasseCategorieTrajet> bases = tarifClasseCategorieTrajetService.findByTrajetIdAndDate(trajetId, voyageDate);
            List<TarifSpecialCategorie> specials = tarifSpecialCategorieService.findByClasseCategorieAndCategorie(classeCategorie.getIdClasseCategorie(), categorie.getIdCategorie());
            for (TarifSpecialCategorie tsc : specials) {
                java.math.BigDecimal price = tarifSpecialCategorieService.computeSpecialPrice(tsc, bases, null);
                if (price != null) out.add(new PriceResult(price, "tarif_special_categorie (remise=" + tsc.getRemise() + "% )"));
            }
        }

        // 2) tarif_classe_categorie_trajet (could be multiple entries)
        if (classeCategorie != null && classeCategorie.getIdClasseCategorie() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            for (TarifClasseCategorieTrajet tcc : tarifClasseCategorieTrajetService.findByTrajetIdAndDate(trajetId, voyageDate)) {
                if (tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getIdClasseCategorie() != null
                        && tcc.getClasseCategorie().getIdClasseCategorie().equals(classeCategorie.getIdClasseCategorie())
                        && tcc.getMontant() != null) out.add(new PriceResult(tcc.getMontant().setScale(2, RoundingMode.HALF_UP), "tarif_classe_categorie_trajet"));
            }
        }

        // 3) tarif_classe_trajet
        if (classeCategorie != null && classeCategorie.getClasse() != null && classeCategorie.getClasse().getIdClasse() != null) {
            Integer trajetId = (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null)
                    ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            for (TarifClasseTrajet tct : tarifClasseTrajetService.findByTrajetIdAndDate(trajetId, voyageDate)) {
                if (tct.getClasse() != null && tct.getClasse().getIdClasse() != null
                        && tct.getClasse().getIdClasse().equals(classeCategorie.getClasse().getIdClasse())
                        && tct.getMontant() != null) out.add(new PriceResult(tct.getMontant().setScale(2, RoundingMode.HALF_UP), "tarif_classe_trajet"));
            }
        }

        // 4) voyageVehicule.prix -> voyage.prix -> trajet.prix (each as an option if present)
        if (voyageVehicule.getPrix() != null) out.add(new PriceResult(BigDecimal.valueOf(voyageVehicule.getPrix()).setScale(2, RoundingMode.HALF_UP), "voyageVehicule.prix"));
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getPrix() != null)
            out.add(new PriceResult(BigDecimal.valueOf(voyageVehicule.getVoyage().getPrix()).setScale(2, RoundingMode.HALF_UP), "voyage.prix"));
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null
                && voyageVehicule.getVoyage().getTrajet().getPrix() != null)
            out.add(new PriceResult(BigDecimal.valueOf(voyageVehicule.getVoyage().getTrajet().getPrix()).setScale(2, RoundingMode.HALF_UP), "trajet.prix"));

        // de-duplicate by price+source string
        List<PriceResult> dedup = new java.util.ArrayList<>();
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (PriceResult pr : out) {
            String key = pr.price.setScale(2, RoundingMode.HALF_UP).toString() + "|" + (pr.source != null ? pr.source : "") + "|" + (pr.label != null ? pr.label : "");
            if (!seen.contains(key)) { seen.add(key); dedup.add(pr); }
        }
        return dedup;
    }

    /**
     * Collect price options for all classes/categories applicable to the given VoyageVehicule.
     * Precedence for display: if any tarif_classe_categorie_trajet exist for the voyage's trajet/date,
     * return those (including any tarif_special_categorie discounts applied). Otherwise, if any
     * tarif_classe_trajet exist for the trajet/date, return those. Otherwise return the base
     * voyageVehicule/voyage/trajet price as a single option (if present).
     */
    public List<PriceResult> collectAllOptionsForVoyageVehicule(VoyageVehicule voyageVehicule) {
        List<PriceResult> out = new java.util.ArrayList<>();
        if (voyageVehicule == null) return out;
        final java.time.LocalDate voyageDate = (voyageVehicule.getVoyage() != null) ? voyageVehicule.getVoyage().getDateDepart() : null;

        // log counts from tariff tables
        try {
            int cntTsc = tarifSpecialCategorieService.findAll() != null ? tarifSpecialCategorieService.findAll().size() : 0;
            int cntTccAll = tarifClasseCategorieTrajetService.findAll() != null ? tarifClasseCategorieTrajetService.findAll().size() : 0;
            int cntTctAll = tarifClasseTrajetService.findAll() != null ? tarifClasseTrajetService.findAll().size() : 0;
            Integer vvId = voyageVehicule.getIdVoyageVehicule();
            Integer trajetId = voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
            logger.info("Tariff tables counts: tarif_special_categorie={}, tarif_classe_categorie_trajet(all)={}, tarif_classe_trajet(all)={}; voyageVehiculeId={} trajetId={} date={}", cntTsc, cntTccAll, cntTctAll, vvId, trajetId, voyageDate);
        } catch (Exception e) {
            logger.info("Could not read tariff table sizes: {}", e.getMessage());
        }

        // gather all tarif_classe_categorie_trajet for this trajet/date via helper
        Integer trajetId = voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null
                ? voyageVehicule.getVoyage().getTrajet().getIdTrajet() : null;
        List<TarifClasseCategorieTrajet> tccList = tarifClasseCategorieTrajetService.findByTrajetIdAndDate(trajetId, voyageDate);
        if (!tccList.isEmpty()) {
            // log details of each matching tarif_classe_categorie_trajet for easier debugging
            for (TarifClasseCategorieTrajet tcc : tccList) {
                try {
                    String cl = tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getClasse() != null ? tcc.getClasseCategorie().getClasse().getLibelle() : "(no-classe)";
                    String ca = tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getCategorie() != null ? tcc.getClasseCategorie().getCategorie().getLibelle() : "(no-categorie)";
                    logger.info("Found tcc entry: classe={}, categorie={}, montant={}", cl, ca, tcc.getMontant());
                } catch (Exception ignore) {}
            }
        }
        if (!tccList.isEmpty()) {
            logger.info("Found {} tarif_classe_categorie_trajet matching trajet/date", tccList.size());
            for (TarifClasseCategorieTrajet tcc : tccList) {
                Integer classeCategorieId = tcc.getClasseCategorie() != null ? tcc.getClasseCategorie().getIdClasseCategorie() : null;
                Integer tccCatId = tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getCategorie() != null
                        ? tcc.getClasseCategorie().getCategorie().getIdCategorie() : null;
                // compute label from classe/categorie if available
                String label = null;
                try {
                    if (tcc.getClasseCategorie() != null && tcc.getClasseCategorie().getClasse() != null
                            && tcc.getClasseCategorie().getCategorie() != null) {
                        String cl = tcc.getClasseCategorie().getClasse().getLibelle();
                        String ca = tcc.getClasseCategorie().getCategorie().getLibelle();
                        label = (cl != null ? cl : "") + " / " + (ca != null ? ca : "");
                    }
                } catch (Exception ignore) {}

                if (classeCategorieId != null && tccCatId != null) {
                    System.out.println("======= ato eeeee =====");
                    System.out.println("classeCategorieId=" + classeCategorieId + ", tccCatId=" + tccCatId);
                    List<TarifSpecialCategorie> specials = tarifSpecialCategorieService.findByClasseAndCategorie(tcc.getClasseCategorie().getClasse().getIdClasse(), tccCatId);
                    System.out.println("======= specials size: " + specials.size() + " =====");
                    for (TarifSpecialCategorie tsc : specials) {
                        java.math.BigDecimal price = tarifSpecialCategorieService.computeSpecialPrice(tsc, tccList, tcc.getMontant());
                        if (price != null) out.add(new PriceResult(price, "tarif_special_categorie (remise=" + tsc.getRemise() + "% )", label));
                    }
                }
                out.add(new PriceResult(tcc.getMontant().setScale(2, java.math.RoundingMode.HALF_UP), "tarif_classe_categorie_trajet", label));
            }
            // dedupe
            List<PriceResult> dedup = new java.util.ArrayList<>();
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (PriceResult pr : out) {
                String key = pr.price.setScale(2, java.math.RoundingMode.HALF_UP).toString() + "|" + (pr.source != null ? pr.source : "") + "|" + (pr.label != null ? pr.label : "");
                if (!seen.contains(key)) { seen.add(key); dedup.add(pr); }
            }

            // Group by label and choose a single price per label.
            // If any special tariffs exist for the label, prefer the best special (lowest price).
            java.util.Map<String, java.util.List<PriceResult>> groups = new java.util.HashMap<>();
            for (PriceResult pr : dedup) {
                String gkey = pr.label != null ? pr.label : (pr.source != null ? pr.source : "__NO_LABEL__");
                groups.computeIfAbsent(gkey, k -> new java.util.ArrayList<>()).add(pr);
            }
            List<PriceResult> finalList = new java.util.ArrayList<>();
            for (java.util.Map.Entry<String, java.util.List<PriceResult>> e : groups.entrySet()) {
                java.util.List<PriceResult> grp = e.getValue();
                // collect special tariffs in this group
                java.util.List<PriceResult> specials = new java.util.ArrayList<>();
                for (PriceResult p : grp) {
                    if (p.source != null && p.source.startsWith("tarif_special_categorie")) specials.add(p);
                }
                if (!specials.isEmpty()) {
                    // pick the best special price (lowest) to overwrite others
                    PriceResult best = specials.get(0);
                    for (PriceResult s : specials) {
                        if (s.price.compareTo(best.price) < 0) best = s;
                    }
                    finalList.add(best);
                } else {
                    // no specials: pick the lowest price among group to avoid duplicates
                    PriceResult best = grp.get(0);
                    for (PriceResult p : grp) {
                        if (p.price.compareTo(best.price) < 0) best = p;
                    }
                    finalList.add(best);
                }
            }
            // sort by classe then categorie (label format: "Classe / Categorie")
            finalList.sort(java.util.Comparator
                    .comparing((PriceResult p) -> {
                        String lbl = p.label != null ? p.label : "";
                        String[] parts = lbl.split("\\s*/\\s*", 2);
                        return parts.length > 0 ? parts[0].toLowerCase() : "";
                    })
                    .thenComparing(p -> {
                        String lbl = p.label != null ? p.label : "";
                        String[] parts = lbl.split("\\s*/\\s*", 2);
                        return parts.length > 1 ? parts[1].toLowerCase() : "";
                    })
                    .thenComparing(p -> p.price)
            );
            return finalList;
        }

        // if no classe+categorie tariffs, gather tarif_classe_trajet via helper
        List<TarifClasseTrajet> tctList = tarifClasseTrajetService.findByTrajetIdAndDate(trajetId, voyageDate);
        if (!tctList.isEmpty()) {
            logger.info("Found {} tarif_classe_trajet matching trajet/date", tctList.size());
            List<PriceResult> out2 = new java.util.ArrayList<>();
            for (TarifClasseTrajet tct : tctList) {
                String label = null;
                try {
                    if (tct.getClasse() != null) {
                        String cl = tct.getClasse().getLibelle();
                        label = (cl != null ? cl : "") + " / ";
                    }
                } catch (Exception ignore) {}
                out2.add(new PriceResult(tct.getMontant().setScale(2, java.math.RoundingMode.HALF_UP), "tarif_classe_trajet", label));
            }
            // dedupe
            List<PriceResult> dedup2 = new java.util.ArrayList<>();
            java.util.Set<String> seen2 = new java.util.HashSet<>();
            for (PriceResult pr : out2) {
                String key = pr.price.setScale(2, java.math.RoundingMode.HALF_UP).toString() + "|" + (pr.source != null ? pr.source : "") + "|" + (pr.label != null ? pr.label : "");
                if (!seen2.contains(key)) { seen2.add(key); dedup2.add(pr); }
            }
            return dedup2;
        }

        // fallback to base prices
        if (voyageVehicule.getPrix() != null) {
            System.out.println("ato no tafiditra");
            logger.info("Using voyageVehicule.prix = {} for voyageVehiculeId={}", voyageVehicule.getPrix(), voyageVehicule.getIdVoyageVehicule());
            return java.util.Collections.singletonList(new PriceResult(java.math.BigDecimal.valueOf(voyageVehicule.getPrix()).setScale(2, java.math.RoundingMode.HALF_UP), "voyageVehicule.prix"));
        }
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getPrix() != null) {
            logger.info("Using voyage.prix = {} for voyageVehiculeId={}", voyageVehicule.getVoyage().getPrix(), voyageVehicule.getIdVoyageVehicule());
            return java.util.Collections.singletonList(new PriceResult(java.math.BigDecimal.valueOf(voyageVehicule.getVoyage().getPrix()).setScale(2, java.math.RoundingMode.HALF_UP), "voyage.prix"));
        }
        if (voyageVehicule.getVoyage() != null && voyageVehicule.getVoyage().getTrajet() != null && voyageVehicule.getVoyage().getTrajet().getPrix() != null) {
            logger.info("Using trajet.prix = {} for voyageVehiculeId={}", voyageVehicule.getVoyage().getTrajet().getPrix(), voyageVehicule.getIdVoyageVehicule());
            return java.util.Collections.singletonList(new PriceResult(java.math.BigDecimal.valueOf(voyageVehicule.getVoyage().getTrajet().getPrix()).setScale(2, java.math.RoundingMode.HALF_UP), "trajet.prix"));
        }

        return java.util.Collections.emptyList();
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAllByOrderByDateDesc();
    }

    public Optional<Reservation> findById(Integer id) {
        return reservationRepository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteById(Integer id) {
        // Remove linked ReservationDetails first to avoid foreign key constraint errors
        try {
            java.util.List<com.itu.taxibrousse.entity.ReservationDetails> details = reservationDetailsService.findByReservation(id);
            if (details != null) {
                for (com.itu.taxibrousse.entity.ReservationDetails d : details) {
                    if (d != null && d.getIdReservationDetails() != null) {
                        reservationDetailsService.deleteById(d.getIdReservationDetails());
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Could not delete reservation details for reservation id {}: {}", id, e.getMessage());
        }
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findByClient(Integer idClient) {
        return reservationRepository.findByClient_IdClient(idClient);
    }

    public List<Reservation> findWithFilters(Integer id, String dateDebut, String dateFin, 
                                             Integer etat, Integer voyageId, Integer vehiculeId, Integer clientId) {
        LocalDateTime dateDebutParsed = null;
        LocalDateTime dateFinParsed = null;
        
        try {
            if (dateDebut != null && !dateDebut.isEmpty()) {
                dateDebutParsed = LocalDateTime.parse(dateDebut + "T00:00:00");
            }
            if (dateFin != null && !dateFin.isEmpty()) {
                dateFinParsed = LocalDateTime.parse(dateFin + "T23:59:59");
            }
        } catch (Exception e) {
            logger.warn("Erreur lors du parsing des dates: " + e.getMessage());
        }
        
        return reservationRepository.findWithFilters(id, dateDebutParsed, dateFinParsed, etat, voyageId, vehiculeId, clientId);
    }

    public List<Reservation> findByVoyageVehicule(Integer idVoyageVehicule) {
        return reservationRepository.findByVoyageVehicule_IdVoyageVehicule(idVoyageVehicule);
    }

    public List<Reservation> findByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) {
        return reservationRepository.findByDateBetween(dateDebut, dateFin);
    }

    /**
     * Helper: given a TarifSpecialCategorie and the list of TarifClasseCategorieTrajet
     * candidates for the voyage/trajet/date, find the appropriate base montant and
     * apply the remise. If no dedicated base is found, use the provided fallbackMontant
     * (may be null). Returns the discounted price or null.
     */
    
    public BigDecimal calculateRevenue(Integer voyageVehiculeId) {
        List<Reservation> reservations = findByVoyageVehicule(voyageVehiculeId);
        return reservations.stream()
            .map(this::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calculateRevenueByMonth(Integer mois, Integer annee) {
        List<Reservation> reservations = reservationRepository.findByMonthAndYear(mois, annee);
        return reservations.stream()
            .map(this::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getTotalRevenue() {
        List<Reservation> reservations = findAll();
        return reservations.stream()
            .map(this::getMontantTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

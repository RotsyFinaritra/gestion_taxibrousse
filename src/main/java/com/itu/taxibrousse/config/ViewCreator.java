package com.itu.taxibrousse.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class ViewCreator {

    private final JdbcTemplate jdbc;

    public ViewCreator(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void createViews() {
        try {
            String sql = "CREATE OR REPLACE VIEW reservation_details_lib AS " +
                    "SELECT rd.id_reservation_details, rd.num_place, rd.montant, rd.etat, rd.id_reservation, rd.id_classe_categorie, " +
                    "c.libelle AS classe_libelle, cat.libelle AS categorie_libelle, v.date_depart AS voyage_date, t.id_trajet AS trajet_id " +
                    "FROM reservation_details rd " +
                    "LEFT JOIN classe_categorie cc ON cc.id_classe_categorie = rd.id_classe_categorie " +
                    "LEFT JOIN classe c ON c.id_classe = cc.id_classe " +
                    "LEFT JOIN categorie cat ON cat.id_categorie = cc.id_categorie " +
                    "LEFT JOIN reservation r ON r.id_reservation = rd.id_reservation " +
                    "LEFT JOIN voyage_vehicule vv ON vv.id_voyage_vehicule = r.id_voyage_vehicule " +
                    "LEFT JOIN voyage v ON v.id_voyage = vv.id_voyage " +
                    "LEFT JOIN trajet t ON t.id_trajet = v.id_trajet;";
            jdbc.execute(sql);
        } catch (Exception e) {
            // Log at debug to avoid failing startup on environments without DB connection during build-time tasks
            System.err.println("Could not create view reservation_details_lib: " + e.getMessage());
        }
    }
}

-- Migration vers la nouvelle structure diffusion_publicitaire
-- Étape 1: Renommer l'ancienne table
ALTER TABLE diffusion_publicitaire RENAME TO diffusion_publicitaire_old;

-- Étape 2: Créer les nouvelles tables
CREATE TABLE diffusion_publicitaire (
    id_diffusion_publicitaire SERIAL PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_cloturage DATE,
    id_societe INTEGER NOT NULL,
    FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
);

CREATE TABLE diffusion_pub_details (
    id_diffusion_pub_details SERIAL PRIMARY KEY,
    date_diffusion TIMESTAMP NOT NULL,
    nb_diffusion INTEGER NOT NULL DEFAULT 1,
    id_diffusion_publicitaire INTEGER NOT NULL,
    id_voyage_vehicule INTEGER NOT NULL,
    id_video_publicitaire INTEGER NOT NULL,
    FOREIGN KEY (id_diffusion_publicitaire) REFERENCES diffusion_publicitaire(id_diffusion_publicitaire),
    FOREIGN KEY (id_voyage_vehicule) REFERENCES voyage_vehicule(id_voyage_vehicule),
    FOREIGN KEY (id_video_publicitaire) REFERENCES video_publicitaire(id_video_publicitaire)
);

-- Étape 3: Migrer les données existantes
INSERT INTO diffusion_publicitaire (date_debut, date_cloturage, id_societe)
SELECT 
    DATE(dpo.date_diffusion) as date_debut,
    NULL as date_cloturage,
    vp.id_societe
FROM diffusion_publicitaire_old dpo
JOIN video_publicitaire vp ON dpo.id_video_publicitaire = vp.id_video_publicitaire
GROUP BY DATE(dpo.date_diffusion), vp.id_societe
ORDER BY date_debut;

-- Étape 4: Migrer les détails
INSERT INTO diffusion_pub_details (date_diffusion, nb_diffusion, id_diffusion_publicitaire, id_voyage_vehicule, id_video_publicitaire)
SELECT 
    dpo.date_diffusion,
    COALESCE(dpo.nb_diffusion, 1) as nb_diffusion,
    dp.id_diffusion_publicitaire,
    dpo.id_voyage_vehicule,
    dpo.id_video_publicitaire
FROM diffusion_publicitaire_old dpo
JOIN video_publicitaire vp ON dpo.id_video_publicitaire = vp.id_video_publicitaire
JOIN diffusion_publicitaire dp ON DATE(dpo.date_diffusion) = dp.date_debut AND vp.id_societe = dp.id_societe;

-- Étape 5: Supprimer l'ancienne table (optionnel, à faire une fois que tout fonctionne)
-- DROP TABLE diffusion_publicitaire_old;
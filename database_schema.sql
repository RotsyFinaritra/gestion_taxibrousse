-- ========================================
-- Script de création de la base de données
-- Projet: TaxiBrousse
-- Date: 30 janvier 2026
-- Description: Script SQL généré à partir des entités JPA
-- ========================================

-- Tables de base (sans dépendances)
-- ========================================

-- Table: ville
CREATE TABLE ville (
    id_ville SERIAL PRIMARY KEY,
    nom VARCHAR(50)
);

-- Table: heure_depart
CREATE TABLE heure_depart (
    id_heure_depart SERIAL PRIMARY KEY,
    heure TIME
);

-- Table: user_admin
CREATE TABLE user_admin (
    id_user SERIAL PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

-- Table: client
CREATE TABLE client (
    id_client SERIAL PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

-- Table: classe
CREATE TABLE classe (
    id_classe SERIAL PRIMARY KEY,
    libelle VARCHAR(50),
    couleur VARCHAR(16)
);

-- Table: categorie
CREATE TABLE categorie (
    id_categorie SERIAL PRIMARY KEY,
    libelle VARCHAR(50),
    couleur VARCHAR(20)
);

-- Table: vehicule
CREATE TABLE vehicule (
    id_vehicule SERIAL PRIMARY KEY,
    modele VARCHAR(50),
    marque VARCHAR(50),
    immatriculation VARCHAR(50),
    capacite INTEGER
);

-- Table: societe
CREATE TABLE societe (
    id_societe SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    contact VARCHAR(50),
    email VARCHAR(50),
    tarif_par_diffusion NUMERIC(15, 2),
    date_creation DATE
);

-- Table: produit
CREATE TABLE produit (
    id_produit SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    prix NUMERIC(15, 2)
);

-- Tables avec dépendances de niveau 1
-- ========================================

-- Table: gare_routiere
CREATE TABLE gare_routiere (
    id_gare_routiere SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    id_ville INTEGER NOT NULL,
    CONSTRAINT fk_gare_ville FOREIGN KEY (id_ville) REFERENCES ville(id_ville)
);

-- Table: classe_categorie
CREATE TABLE classe_categorie (
    id_classe_categorie SERIAL PRIMARY KEY,
    id_categorie INTEGER NOT NULL,
    id_classe INTEGER NOT NULL,
    CONSTRAINT fk_classecategorie_categorie FOREIGN KEY (id_categorie) REFERENCES categorie(id_categorie),
    CONSTRAINT fk_classecategorie_classe FOREIGN KEY (id_classe) REFERENCES classe(id_classe)
);

-- Table: classe_siege_vehicule
CREATE TABLE classe_siege_vehicule (
    id_classe_siege_vehicule SERIAL PRIMARY KEY,
    num_place INTEGER,
    id_vehicule INTEGER NOT NULL,
    id_classe INTEGER NOT NULL,
    CONSTRAINT fk_classesiege_vehicule FOREIGN KEY (id_vehicule) REFERENCES vehicule(id_vehicule),
    CONSTRAINT fk_classesiege_classe FOREIGN KEY (id_classe) REFERENCES classe(id_classe)
);

-- Table: video_publicitaire
CREATE TABLE video_publicitaire (
    id_video_publicitaire SERIAL PRIMARY KEY,
    titre VARCHAR(50),
    url_fichier VARCHAR(50),
    date_ajout DATE,
    id_societe INTEGER NOT NULL,
    CONSTRAINT fk_video_societe FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
);

-- Table: historique_tarif_diffusion
CREATE TABLE historique_tarif_diffusion (
    id_historique_tarif SERIAL PRIMARY KEY,
    tarif NUMERIC(15, 2) NOT NULL,
    date_ DATE NOT NULL,
    id_societe INTEGER NOT NULL,
    CONSTRAINT fk_historiquetarif_societe FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
);

-- Table: historique_prix_produit
CREATE TABLE historique_prix_produit (
    id_historique_prix_produit SERIAL PRIMARY KEY,
    montant NUMERIC(15, 2),
    date_ DATE,
    id_produit INTEGER NOT NULL,
    CONSTRAINT fk_historiqueprix_produit FOREIGN KEY (id_produit) REFERENCES produit(id_produit)
);

-- Tables avec dépendances de niveau 2
-- ========================================

-- Table: trajet
CREATE TABLE trajet (
    id_trajet SERIAL PRIMARY KEY,
    prix DOUBLE PRECISION,
    distance DOUBLE PRECISION,
    id_gare_routiere_depart INTEGER NOT NULL,
    id_gare_routiere_arrivee INTEGER NOT NULL,
    CONSTRAINT fk_trajet_gare_depart FOREIGN KEY (id_gare_routiere_depart) REFERENCES gare_routiere(id_gare_routiere),
    CONSTRAINT fk_trajet_gare_arrivee FOREIGN KEY (id_gare_routiere_arrivee) REFERENCES gare_routiere(id_gare_routiere)
);

-- Table: tarif_classe_trajet
CREATE TABLE tarif_classe_trajet (
    id_tarif_classe_trajet SERIAL PRIMARY KEY,
    montant NUMERIC(15, 2),
    date_ DATE,
    id_classe INTEGER NOT NULL,
    id_trajet INTEGER NOT NULL,
    CONSTRAINT fk_tarifclasse_classe FOREIGN KEY (id_classe) REFERENCES classe(id_classe),
    CONSTRAINT fk_tarifclasse_trajet FOREIGN KEY (id_trajet) REFERENCES trajet(id_trajet)
);

-- Table: tarif_classe_categorie_trajet
CREATE TABLE tarif_classe_categorie_trajet (
    id_tarif_classe_categorie_trajet SERIAL PRIMARY KEY,
    montant NUMERIC(15, 2),
    date_ DATE,
    id_classe_categorie INTEGER NOT NULL,
    id_trajet INTEGER NOT NULL,
    CONSTRAINT fk_tarifclassecategorie_classecategorie FOREIGN KEY (id_classe_categorie) REFERENCES classe_categorie(id_classe_categorie),
    CONSTRAINT fk_tarifclassecategorie_trajet FOREIGN KEY (id_trajet) REFERENCES trajet(id_trajet)
);

-- Table: tarif_special_categorie
CREATE TABLE tarif_special_categorie (
    id_tarif_sepcial_categorie SERIAL PRIMARY KEY,
    remise NUMERIC(5, 2),
    id_classe_categorie INTEGER NOT NULL,
    id_categorie INTEGER NOT NULL,
    CONSTRAINT fk_tarifspecial_classecategorie FOREIGN KEY (id_classe_categorie) REFERENCES classe_categorie(id_classe_categorie),
    CONSTRAINT fk_tarifspecial_categorie FOREIGN KEY (id_categorie) REFERENCES categorie(id_categorie)
);

-- Tables avec dépendances de niveau 3
-- ========================================

-- Table: voyage
CREATE TABLE voyage (
    id_voyage SERIAL PRIMARY KEY,
    date_depart DATE,
    prix DOUBLE PRECISION,
    id_heure_depart INTEGER NOT NULL,
    id_trajet INTEGER NOT NULL,
    CONSTRAINT fk_voyage_heuredepart FOREIGN KEY (id_heure_depart) REFERENCES heure_depart(id_heure_depart),
    CONSTRAINT fk_voyage_trajet FOREIGN KEY (id_trajet) REFERENCES trajet(id_trajet)
);

-- Tables avec dépendances de niveau 4
-- ========================================

-- Table: voyage_vehicule
CREATE TABLE voyage_vehicule (
    id_voyage_vehicule SERIAL PRIMARY KEY,
    prix DOUBLE PRECISION,
    id_voyage INTEGER NOT NULL,
    id_vehicule INTEGER NOT NULL,
    CONSTRAINT fk_voyagevehicule_voyage FOREIGN KEY (id_voyage) REFERENCES voyage(id_voyage),
    CONSTRAINT fk_voyagevehicule_vehicule FOREIGN KEY (id_vehicule) REFERENCES vehicule(id_vehicule)
);

-- Table: diffusion_publicitaire
CREATE TABLE diffusion_publicitaire (
    id_diffusion_publicitaire SERIAL PRIMARY KEY,
    date_debut DATE,
    date_cloturage DATE,
    id_societe INTEGER NOT NULL,
    CONSTRAINT fk_diffusion_societe FOREIGN KEY (id_societe) REFERENCES societe(id_societe)
);

-- Tables avec dépendances de niveau 5
-- ========================================

-- Table: reservation
CREATE TABLE reservation (
    id_reservation SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    contact VARCHAR(50),
    nombre_place INTEGER,
    date_ TIMESTAMP,
    etat INTEGER,
    id_client INTEGER,
    id_voyage_vehicule INTEGER NOT NULL,
    CONSTRAINT fk_reservation_client FOREIGN KEY (id_client) REFERENCES client(id_client),
    CONSTRAINT fk_reservation_voyagevehicule FOREIGN KEY (id_voyage_vehicule) REFERENCES voyage_vehicule(id_voyage_vehicule)
);

-- Table: vente_produit
CREATE TABLE vente_produit (
    id_vente_produit SERIAL PRIMARY KEY,
    quantite INTEGER,
    date_ DATE,
    id_voyage_vehicule INTEGER NOT NULL,
    CONSTRAINT fk_venteproduit_voyagevehicule FOREIGN KEY (id_voyage_vehicule) REFERENCES voyage_vehicule(id_voyage_vehicule)
);

-- Table: diffusion_pub_details
CREATE TABLE diffusion_pub_details (
    id_diffusion_pub_details SERIAL PRIMARY KEY,
    date_diffusion TIMESTAMP,
    nb_diffusion INTEGER,
    id_diffusion_publicitaire INTEGER NOT NULL,
    id_voyage_vehicule INTEGER NOT NULL,
    id_video_publicitaire INTEGER NOT NULL,
    CONSTRAINT fk_diffusiondetails_diffusion FOREIGN KEY (id_diffusion_publicitaire) REFERENCES diffusion_publicitaire(id_diffusion_publicitaire),
    CONSTRAINT fk_diffusiondetails_voyagevehicule FOREIGN KEY (id_voyage_vehicule) REFERENCES voyage_vehicule(id_voyage_vehicule),
    CONSTRAINT fk_diffusiondetails_video FOREIGN KEY (id_video_publicitaire) REFERENCES video_publicitaire(id_video_publicitaire)
);

-- Table: paiement_diffusion
CREATE TABLE paiement_diffusion (
    id_paiement_diffusion SERIAL PRIMARY KEY,
    montant NUMERIC(15, 2),
    date DATE,
    mois_annee_a_payer DATE,
    id_diffusion_pub INTEGER NOT NULL,
    CONSTRAINT fk_paiementdiffusion_diffusion FOREIGN KEY (id_diffusion_pub) REFERENCES diffusion_publicitaire(id_diffusion_publicitaire)
);

-- Tables avec dépendances de niveau 6
-- ========================================

-- Table: reservation_details
CREATE TABLE reservation_details (
    id_reservation_details SERIAL PRIMARY KEY,
    num_place INTEGER,
    montant DOUBLE PRECISION,
    etat INTEGER,
    id_reservation INTEGER NOT NULL,
    id_classe_categorie INTEGER,
    CONSTRAINT fk_reservationdetails_reservation FOREIGN KEY (id_reservation) REFERENCES reservation(id_reservation),
    CONSTRAINT fk_reservationdetails_classecategorie FOREIGN KEY (id_classe_categorie) REFERENCES classe_categorie(id_classe_categorie)
);

-- Table: vente_details
CREATE TABLE vente_details (
    id_vente_details SERIAL PRIMARY KEY,
    date_ DATE,
    quantite INTEGER,
    id_produit INTEGER NOT NULL,
    id_vente_produit INTEGER NOT NULL,
    CONSTRAINT fk_ventedetails_produit FOREIGN KEY (id_produit) REFERENCES produit(id_produit),
    CONSTRAINT fk_ventedetails_venteproduit FOREIGN KEY (id_vente_produit) REFERENCES vente_produit(id_vente_produit)
);

-- Table: paiement_details
CREATE TABLE paiement_details (
    id_paiement_details SERIAL PRIMARY KEY,
    montant NUMERIC(15, 2),
    date DATE,
    id_paiement INTEGER NOT NULL,
    id_diffusion_details INTEGER NOT NULL,
    CONSTRAINT fk_paiementdetails_paiement FOREIGN KEY (id_paiement) REFERENCES paiement_diffusion(id_paiement_diffusion),
    CONSTRAINT fk_paiementdetails_diffusiondetails FOREIGN KEY (id_diffusion_details) REFERENCES diffusion_pub_details(id_diffusion_pub_details)
);

-- ========================================
-- Indexes recommandés pour les performances
-- ========================================

-- Index sur les clés étrangères
CREATE INDEX idx_gare_ville ON gare_routiere(id_ville);
CREATE INDEX idx_trajet_gare_depart ON trajet(id_gare_routiere_depart);
CREATE INDEX idx_trajet_gare_arrivee ON trajet(id_gare_routiere_arrivee);
CREATE INDEX idx_voyage_trajet ON voyage(id_trajet);
CREATE INDEX idx_voyage_heure ON voyage(id_heure_depart);
CREATE INDEX idx_voyagevehicule_voyage ON voyage_vehicule(id_voyage);
CREATE INDEX idx_voyagevehicule_vehicule ON voyage_vehicule(id_vehicule);
CREATE INDEX idx_reservation_client ON reservation(id_client);
CREATE INDEX idx_reservation_voyagevehicule ON reservation(id_voyage_vehicule);
CREATE INDEX idx_video_societe ON video_publicitaire(id_societe);
CREATE INDEX idx_diffusion_societe ON diffusion_publicitaire(id_societe);

-- Index sur les dates pour les requêtes temporelles
CREATE INDEX idx_voyage_date ON voyage(date_depart);
CREATE INDEX idx_reservation_date ON reservation(date_);
CREATE INDEX idx_venteproduit_date ON vente_produit(date_);
CREATE INDEX idx_diffusion_date_debut ON diffusion_publicitaire(date_debut);
CREATE INDEX idx_diffusion_date_cloturage ON diffusion_publicitaire(date_cloturage);

-- Index sur les champs de recherche fréquents
CREATE INDEX idx_vehicule_immatriculation ON vehicule(immatriculation);
CREATE INDEX idx_ville_nom ON ville(nom);
CREATE INDEX idx_client_username ON client(username);
CREATE INDEX idx_useradmin_username ON user_admin(username);

-- ========================================
-- Commentaires sur les tables
-- ========================================

COMMENT ON TABLE ville IS 'Table des villes';
COMMENT ON TABLE gare_routiere IS 'Table des gares routières';
COMMENT ON TABLE trajet IS 'Table des trajets entre gares';
COMMENT ON TABLE voyage IS 'Table des voyages programmés';
COMMENT ON TABLE vehicule IS 'Table des véhicules';
COMMENT ON TABLE voyage_vehicule IS 'Association entre voyages et véhicules';
COMMENT ON TABLE classe IS 'Table des classes de sièges (Économique, VIP, etc.)';
COMMENT ON TABLE categorie IS 'Table des catégories de passagers (Adulte, Enfant, etc.)';
COMMENT ON TABLE classe_categorie IS 'Association entre classes et catégories';
COMMENT ON TABLE classe_siege_vehicule IS 'Configuration des sièges par classe dans les véhicules';
COMMENT ON TABLE tarif_classe_trajet IS 'Historique des tarifs par classe et trajet';
COMMENT ON TABLE tarif_classe_categorie_trajet IS 'Historique des tarifs par classe-catégorie et trajet';
COMMENT ON TABLE tarif_special_categorie IS 'Remises spéciales par catégorie';
COMMENT ON TABLE client IS 'Table des clients';
COMMENT ON TABLE reservation IS 'Table des réservations';
COMMENT ON TABLE reservation_details IS 'Détails des réservations (places)';
COMMENT ON TABLE societe IS 'Table des sociétés publicitaires';
COMMENT ON TABLE video_publicitaire IS 'Table des vidéos publicitaires';
COMMENT ON TABLE diffusion_publicitaire IS 'Campagnes de diffusion publicitaire';
COMMENT ON TABLE diffusion_pub_details IS 'Détails des diffusions par voyage-véhicule';
COMMENT ON TABLE paiement_diffusion IS 'Paiements des diffusions publicitaires';
COMMENT ON TABLE paiement_details IS 'Détails des paiements';
COMMENT ON TABLE historique_tarif_diffusion IS 'Historique des tarifs de diffusion';
COMMENT ON TABLE produit IS 'Table des produits vendus';
COMMENT ON TABLE vente_produit IS 'Ventes de produits par voyage';
COMMENT ON TABLE vente_details IS 'Détails des ventes de produits';
COMMENT ON TABLE historique_prix_produit IS 'Historique des prix des produits';
COMMENT ON TABLE heure_depart IS 'Heures de départ disponibles';
COMMENT ON TABLE user_admin IS 'Utilisateurs administrateurs';

-- ========================================
-- Fin du script
-- ========================================

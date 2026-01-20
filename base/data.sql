-- Script de données de test pour Taxi Brousse
-- Base de données: taxibrousse_v2

-- ============================================
-- NETTOYAGE DES TABLES ET RÉINITIALISATION
-- ============================================

-- Désactiver temporairement les contraintes de clés étrangères
SET session_replication_role = 'replica';

-- Vider toutes les tables et réinitialiser les séquences
TRUNCATE TABLE reservation_details RESTART IDENTITY CASCADE;
TRUNCATE TABLE reservation RESTART IDENTITY CASCADE;
TRUNCATE TABLE voyage_vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE voyage RESTART IDENTITY CASCADE;
TRUNCATE TABLE trajet RESTART IDENTITY CASCADE;
TRUNCATE TABLE gare_routiere RESTART IDENTITY CASCADE;
TRUNCATE TABLE ville RESTART IDENTITY CASCADE;
TRUNCATE TABLE user_admin RESTART IDENTITY CASCADE;
TRUNCATE TABLE heure_depart RESTART IDENTITY CASCADE;
TRUNCATE TABLE vehicule RESTART IDENTITY CASCADE;
TRUNCATE TABLE client RESTART IDENTITY CASCADE;
TRUNCATE TABLE classe_categorie RESTART IDENTITY CASCADE;
TRUNCATE TABLE classe RESTART IDENTITY CASCADE;
TRUNCATE TABLE categorie RESTART IDENTITY CASCADE;

-- Réactiver les contraintes de clés étrangères
SET session_replication_role = 'origin';


-- Réinitialisation des séquences après insertions manuelles
SELECT setval('ville_id_ville_seq', (SELECT COALESCE(MAX(id_ville),0) FROM ville));
SELECT setval('user_admin_id_user_seq', (SELECT COALESCE(MAX(id_user),0) FROM user_admin));
SELECT setval('client_id_client_seq', (SELECT COALESCE(MAX(id_client),0) FROM client));
SELECT setval('heure_depart_id_heure_depart_seq', (SELECT COALESCE(MAX(id_heure_depart),0) FROM heure_depart));
SELECT setval('vehicule_id_vehicule_seq', (SELECT COALESCE(MAX(id_vehicule),0) FROM vehicule));
SELECT setval('gare_routiere_id_gare_routiere_seq', (SELECT COALESCE(MAX(id_gare_routiere),0) FROM gare_routiere));
SELECT setval('trajet_id_trajet_seq', (SELECT COALESCE(MAX(id_trajet),0) FROM trajet));
SELECT setval('voyage_id_voyage_seq', (SELECT COALESCE(MAX(id_voyage),0) FROM voyage));
SELECT setval('voyage_vehicule_id_voyage_vehicule_seq', (SELECT COALESCE(MAX(id_voyage_vehicule),0) FROM voyage_vehicule));
SELECT setval('reservation_id_reservation_seq', (SELECT COALESCE(MAX(id_reservation),0) FROM reservation));
SELECT setval('reservation_details_id_reservation_details_seq', (SELECT COALESCE(MAX(id_reservation_details),0) FROM reservation_details));

SELECT setval('classe_id_classe_seq', (SELECT COALESCE(MAX(id_classe),0) FROM classe));
SELECT setval('categorie_id_categorie_seq', (SELECT COALESCE(MAX(id_categorie),0) FROM categorie));
SELECT setval('classe_categorie_id_classe_categorie_seq', (SELECT COALESCE(MAX(id_classe_categorie),0) FROM classe_categorie));
SELECT setval('tarif_classe_trajet_id_tarif_classe_trajet_seq', (SELECT COALESCE(MAX(id_tarif_classe_trajet),0) FROM tarif_classe_trajet));
SELECT setval('tarif_classe_categorie_trajet_id_tarif_classe_categorie_trajet_seq', (SELECT COALESCE(MAX(id_tarif_classe_categorie_trajet),0) FROM tarif_classe_categorie_trajet));
-- Classes

-- Message de confirmation
SELECT 'Tables vidées et IDs réinitialisés!' as message;

-- ============================================
-- INSERTION DES DONNÉES DE TEST
-- ============================================

-- Villes
INSERT INTO ville (id_ville, nom) VALUES
(1, 'Antananarivo'),
(2, 'Antsirabe'),
(3, 'Fianarantsoa'),
(4, 'Toliara'),
(5, 'Mahajanga'),
(6, 'Toamasina'),
(7, 'Antsiranana'),
(8, 'Ambositra'),
(9, 'Morondava'),
(10, 'Nosy Be');

-- User Admin
INSERT INTO user_admin (id_user, username, password) VALUES
(1, 'admin', 'admin123'),
(2, 'manager', 'manager123'),
(3, 'supervisor', 'super123');

-- Clients
INSERT INTO client (id_client, username, password) VALUES
(1, 'rakoto', 'pass123'),
(2, 'rabe', 'pass456'),
(3, 'rasoa', 'pass789'),
(4, 'ravao', 'pass321'),
(5, 'randrianasolo', 'pass654');

-- Heures de départ
INSERT INTO heure_depart (id_heure_depart, heure) VALUES
(1, '05:00:00'),
(2, '06:00:00'),
(3, '07:30:00'),
(4, '09:00:00'),
(5, '11:00:00'),
(6, '13:00:00'),
(7, '15:00:00'),
(8, '17:00:00');

-- Véhicules
INSERT INTO vehicule (id_vehicule, modele, marque, immatriculation, capacite) VALUES
(1, 'Sprinter', 'Mercedes', '1234 TAB', 15),
(2, 'Hiace', 'Toyota', '5678 TAC', 12),
(3, 'Master', 'Renault', '9012 TAD', 14),
(4, 'Transit', 'Ford', '3456 TAE', 16),
(5, 'Daily', 'Iveco', '7890 TAF', 18),
(6, 'Coaster', 'Toyota', '2345 TAG', 20),
(7, 'Sprinter', 'Mercedes', '6789 TAH', 15),
(8, 'Hiace', 'Toyota', '0123 TAI', 12);

-- Gares Routières
INSERT INTO gare_routiere (id_gare_routiere, nom, id_ville) VALUES
(1, 'Gare Routière Fasan''ny Karana', 1),
(2, 'Gare Sud Ambohidahy', 1),
(3, 'Gare Routière Antsirabe Centre', 2),
(4, 'Gare Routière Fianarantsoa', 3),
(5, 'Gare Routière Toliara', 4),
(6, 'Gare Routière Mahajanga', 5),
(7, 'Gare Routière Toamasina', 6),
(8, 'Gare Routière Antsiranana', 7),
(9, 'Gare Routière Ambositra', 8),
(10, 'Gare Routière Morondava', 9);

-- Trajets
INSERT INTO trajet (id_trajet, prix, distance, id_gare_routiere_depart, id_gare_routiere_arrivee) VALUES
(1, 35000, 170, 1, 3),  -- Antananarivo -> Antsirabe
(2, 60000, 420, 1, 4),  -- Antananarivo -> Fianarantsoa
(3, 25000, 138, 3, 9),  -- Antsirabe -> Ambositra
(4, 120000, 950, 1, 4),  -- Antananarivo -> Toliara (via Fiana)
(5, 80000, 570, 1, 5),  -- Antananarivo -> Mahajanga
(6, 45000, 370, 1, 6),  -- Antananarivo -> Toamasina
(7, 150000, 1080, 1, 7), -- Antananarivo -> Antsiranana
(8, 75000, 630, 1, 9),  -- Antananarivo -> Morondava
(9, 90000, 700, 4, 5),  -- Fianarantsoa -> Toliara
(10, 40000, 250, 3, 4); -- Antsirabe -> Fianarantsoa

-- Voyages
INSERT INTO voyage (id_voyage, date_depart, prix, id_heure_depart, id_trajet) VALUES
(1, '2026-01-20', 35000, 1, 1),   -- Tana-Antsirabe 5h
(2, '2026-01-20', 35000, 3, 1),   -- Tana-Antsirabe 7h30
(3, '2026-01-20', 60000, 2, 2),   -- Tana-Fiana 6h
(4, '2026-01-21', 45000, 1, 6),   -- Tana-Toamasina 5h
(5, '2026-01-21', 80000, 3, 5),   -- Tana-Mahajanga 7h30
(6, '2026-01-22', 25000, 4, 3),   -- Antsirabe-Ambositra 9h
(7, '2026-01-22', 35000, 2, 1),   -- Tana-Antsirabe 6h
(8, '2026-01-23', 75000, 1, 8),   -- Tana-Morondava 5h
(9, '2026-01-23', 150000, 2, 7),  -- Tana-Antsiranana 6h
(10, '2026-01-24', 60000, 3, 2);  -- Tana-Fiana 7h30

-- Voyage-Véhicule (association des véhicules aux voyages)
INSERT INTO voyage_vehicule (id_voyage_vehicule, prix, id_voyage, id_vehicule) VALUES
(1, 35000, 1, 1),   -- Voyage 1 avec Mercedes Sprinter
(2, 35000, 2, 2),   -- Voyage 2 avec Toyota Hiace
(3, 60000, 3, 3),   -- Voyage 3 avec Renault Master
(4, 45000, 4, 4),   -- Voyage 4 avec Ford Transit
(5, 80000, 5, 5),   -- Voyage 5 avec Iveco Daily
(6, 25000, 6, 6),   -- Voyage 6 avec Toyota Coaster
(7, 35000, 7, 7),   -- Voyage 7 avec Mercedes Sprinter
(8, 75000, 8, 8),   -- Voyage 8 avec Toyota Hiace
(9, 150000, 9, 1),  -- Voyage 9 avec Mercedes Sprinter
(10, 60000, 10, 2); -- Voyage 10 avec Toyota Hiace

-- Réservations
INSERT INTO reservation (id_reservation, nom, contact, nombre_place, date_, id_client, id_voyage_vehicule) VALUES
(1, 'RAKOTO Jean', '0340123456', 2, '2026-01-15 10:30:00', 1, 1),
(2, 'RABE Marie', '0331234567', 1, '2026-01-15 14:20:00', 2, 1),
(3, 'RASOA Hanta', '0322345678', 3, '2026-01-16 09:15:00', 3, 2),
(4, 'RAVAO Paul', '0343456789', 2, '2026-01-16 16:45:00', 4, 3),
(5, 'RANDRIANASOLO Lova', '0334567890', 1, '2026-01-17 11:00:00', 5, 4),
(6, 'RAKOTO Jean', '0340123456', 2, '2026-01-17 15:30:00', 1, 5),
(7, 'RABE Marie', '0331234567', 1, '2026-01-18 08:00:00', 2, 6),
(8, 'RASOA Hanta', '0322345678', 4, '2026-01-18 13:20:00', 3, 7);

-- Détails de réservation (numéros de places)
INSERT INTO reservation_details (id_reservation_details, num_place, montant, id_reservation) VALUES
-- Réservation 1 (2 places)
(1, 1, 35000, 1),
(2, 2, 35000, 1),
-- Réservation 2 (1 place)
(3, 3, 35000, 2),
-- Réservation 3 (3 places)
(4, 1, 35000, 3),
(5, 2, 35000, 3),
(6, 3, 35000, 3),
-- Réservation 4 (2 places)
(7, 1, 60000, 4),
(8, 2, 60000, 4),
-- Réservation 5 (1 place)
(9, 1, 45000, 5),
-- Réservation 6 (2 places)
(10, 1, 80000, 6),
(11, 2, 80000, 6),
-- Réservation 7 (1 place)
(12, 1, 25000, 7),
-- Réservation 8 (4 places)
(13, 1, 35000, 8),
(14, 2, 35000, 8),
(15, 3, 35000, 8),
(16, 4, 35000, 8);

INSERT INTO classe (id_classe, libelle) VALUES
(1, 'standard'),
(2, 'premium'),
(3, 'VIP');

-- Catégories
INSERT INTO categorie (id_categorie, libelle) VALUES
(1, 'enfant'),
(2, 'adulte'),
(3, 'senior');

-- Associations classe-categorie
INSERT INTO classe_categorie (id_classe_categorie, id_categorie, id_classe) VALUES
(1, 1, 1), -- enfant-standard
(2, 2, 1), -- adulte-standard
(3, 3, 1), -- senior-standard
(4, 1, 2), -- enfant-premium
(5, 2, 2), -- adulte-premium
(6, 3, 2), -- senior-premium
(7, 1, 3), -- enfant-VIP
(8, 2, 3), -- adulte-VIP
(9, 3, 3); -- senior-VIP


-- Affichage des statistiques
SELECT 'Données insérées avec succès!' as message;
SELECT COUNT(*) as nb_villes FROM ville;
SELECT COUNT(*) as nb_gares FROM gare_routiere;
SELECT COUNT(*) as nb_trajets FROM trajet;
SELECT COUNT(*) as nb_vehicules FROM vehicule;
SELECT COUNT(*) as nb_voyages FROM voyage;
SELECT COUNT(*) as nb_reservations FROM reservation;

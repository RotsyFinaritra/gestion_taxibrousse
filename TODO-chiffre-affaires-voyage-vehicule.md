# TODO: Page Chiffre d'Affaires par Voyage-Véhicule

## 📋 Analyse des besoins pour la page `chiffre-affaire-voyage-vehicule.html`

### 🗄️ Tables de base de données nécessaires

Basé sur l'analyse du template HTML, les tables suivantes semblent nécessaires :

#### Tables principales :
- [ ] **`voyages`** - Table des voyages
- [ ] **`vehicules`** - Table des véhicules (marque, modèle, immatriculation)
- [ ] **`gares_routieres`** - Table des gares routières
- [ ] **`villes`** - Table des villes
- [ ] **`heures`** - Table des heures de départ
- [ ] **`reservations`** ou **`tickets`** - Pour les montants des tickets
- [ ] **`diffusions_publicitaires`** ou **`publicites`** - Pour les montants publicitaires

#### Tables de liaison possibles :
- [ ] **`voyage_vehicules`** - Liaison voyage-véhicule
- [ ] **`trajets`** - Définition des trajets (départ-arrivée)

### 🏗️ Entités Java à créer/vérifier

#### Entité principale pour le DTO :
- [ ] **`ChiffreAffaireVoyageVehicule`** (ou nom similaire)
  ```java
  // Propriétés nécessaires basées sur le template :
  - GareRoutiere gareRoutiereDepart
  - GareRoutiere gareRoutiereArrivee  
  - Vehicule vehicule
  - LocalDate dateDepart
  - String heureDepart (ou LocalTime)
  - BigDecimal montantTickets
  - BigDecimal montantPublicite
  - BigDecimal montantTotal
  ```

#### Entités de base nécessaires :
- [ ] **`GareRoutiere`**
  ```java
  - String nom
  - Ville ville
  ```

- [ ] **`Ville`**
  ```java
  - String nom
  ```

- [ ] **`Vehicule`**
  ```java
  - String marque
  - String modele
  - String immatriculation
  ```

### 🔧 Services et méthodes à implémenter

#### Controller :
- [ ] **`ReportsController`** (ou similaire)
  - [ ] Méthode pour afficher la page : `@GetMapping("/admin/reports/chiffre-affaires-voyage-vehicule")`
    - **Tables utilisées :** Via service (voyage_vehicules, voyages, vehicules, trajets, gares_routieres, villes, heures, reservations, diffusions_publicitaires)

#### Service :
- [ ] **`ChiffreAffairesService`** (ou `ReportsService`)
  - [ ] `List<ChiffreAffaireVoyageVehicule> getChiffreAffairesVoyageVehicule()`
    - **Tables utilisées :** voyage_vehicules, voyages, vehicules, trajets, gares_routieres, villes, heures, reservations, diffusions_publicitaires
  - [ ] `BigDecimal calculateTotalTickets(List<ChiffreAffaireVoyageVehicule> data)`
    - **Tables utilisées :** Calcul en mémoire (pas d'accès direct aux tables)
  - [ ] `BigDecimal calculateTotalPublicite(List<ChiffreAffaireVoyageVehicule> data)`
    - **Tables utilisées :** Calcul en mémoire (pas d'accès direct aux tables)
  - [ ] `BigDecimal calculateTotalGeneral(List<ChiffreAffaireVoyageVehicule> data)`
    - **Tables utilisées :** Calcul en mémoire (pas d'accès direct aux tables)

#### Repository :
- [ ] **Query complexe** pour récupérer les données agrégées
  - **Tables utilisées :** voyage_vehicules, voyages, vehicules, trajets, gares_routieres, villes, heures, reservations, diffusions_publicitaires
  ```sql
  -- Exemple de requête nécessaire :
  SELECT 
    gd.nom as gare_depart_nom, vd.nom as ville_depart_nom,
    ga.nom as gare_arrivee_nom, va.nom as ville_arrivee_nom,
    v.marque, v.modele, v.immatriculation,
    voy.date_depart, h.heure,
    SUM(montant_tickets) as montant_tickets,
    SUM(montant_publicite) as montant_publicite,
    (SUM(montant_tickets) + SUM(montant_publicite)) as montant_total
  FROM voyage_vehicules vv
  JOIN voyages voy ON vv.voyage_id = voy.id
  JOIN vehicules v ON vv.vehicule_id = v.id
  JOIN trajets t ON voy.trajet_id = t.id
  JOIN gares_routieres gd ON t.gare_depart_id = gd.id
  JOIN gares_routieres ga ON t.gare_arrivee_id = ga.id
  JOIN villes vd ON gd.ville_id = vd.id
  JOIN villes va ON ga.ville_id = va.id
  JOIN heures h ON voy.heure_id = h.id
  -- Agrégations des montants depuis les tables de réservations/publicités
  LEFT JOIN reservations r ON vv.id = r.voyage_vehicule_id
  LEFT JOIN diffusions_publicitaires dp ON vv.id = dp.voyage_vehicule_id
  GROUP BY vv.id, gd.nom, vd.nom, ga.nom, va.nom, v.marque, v.modele, v.immatriculation, voy.date_depart, h.heure
  ORDER BY voy.date_depart DESC, h.heure
  ```

#### Méthodes Repository spécifiques :
- [ ] **`findChiffreAffairesVoyageVehicule()`**
  - **Tables utilisées :** voyage_vehicules, voyages, vehicules, trajets, gares_routieres, villes, heures, reservations, diffusions_publicitaires
- [ ] **`findChiffreAffairesVoyageVehiculeByPeriod(LocalDate debut, LocalDate fin)`** (optionnel)
  - **Tables utilisées :** voyage_vehicules, voyages, vehicules, trajets, gares_routieres, villes, heures, reservations, diffusions_publicitaires

### 📊 Données d'affichage requises

#### Variables du modèle Thymeleaf :
- [ ] `chiffreAffaires` - Liste des données principales
- [ ] `totalTickets` - Total des montants tickets
- [ ] `totalPublicite` - Total des montants publicité  
- [ ] `totalGeneral` - Total général (tickets + publicité)

### 🎯 Étapes de développement

1. [ ] **Vérifier/créer les entités de base** (Ville, GareRoutiere, Vehicule, etc.)
2. [ ] **Créer le DTO `ChiffreAffaireVoyageVehicule`**
3. [ ] **Implémenter la requête complexe** dans le repository
4. [ ] **Créer le service** avec les méthodes de calcul
5. [ ] **Créer/compléter le controller** avec l'endpoint
6. [ ] **Tester les données** et l'affichage

### 📝 Notes supplémentaires

- La page utilise Bootstrap pour le style
- Format des montants : avec séparateurs de milliers et 2 décimales (ex: "1,234.56 Ar")
- Gestion du cas "Aucune donnée trouvée" 
- Navigation : bouton retour vers `/admin/reports`
- Responsive design avec classes Bootstrap

---

**Priorité :** 🔴 Haute - Page de reporting importante pour le business
**Estimation :** 2-3 jours de développement
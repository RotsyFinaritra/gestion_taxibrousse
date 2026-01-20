-- Crée la séquence si elle n'existe pas et positionne sa valeur
-- Séquences et defaults pour classe / categorie / classe_categorie
CREATE SEQUENCE IF NOT EXISTS classe_id_classe_seq;
SELECT setval('classe_id_classe_seq', COALESCE((SELECT MAX(id_classe) FROM classe), 0));

ALTER TABLE classe
  ALTER COLUMN id_classe SET DEFAULT nextval('classe_id_classe_seq'),
  ALTER COLUMN id_classe SET NOT NULL;

ALTER SEQUENCE classe_id_classe_seq OWNED BY classe.id_classe;

CREATE SEQUENCE IF NOT EXISTS categorie_id_categorie_seq;
SELECT setval('categorie_id_categorie_seq', COALESCE((SELECT MAX(id_categorie) FROM categorie), 0));

ALTER TABLE categorie
  ALTER COLUMN id_categorie SET DEFAULT nextval('categorie_id_categorie_seq'),
  ALTER COLUMN id_categorie SET NOT NULL;

ALTER SEQUENCE categorie_id_categorie_seq OWNED BY categorie.id_categorie;

CREATE SEQUENCE IF NOT EXISTS classe_categorie_id_classe_categorie_seq;
SELECT setval('classe_categorie_id_classe_categorie_seq', COALESCE((SELECT MAX(id_classe_categorie) FROM classe_categorie), 0));

ALTER TABLE classe_categorie
  ALTER COLUMN id_classe_categorie SET DEFAULT nextval('classe_categorie_id_classe_categorie_seq'),
  ALTER COLUMN id_classe_categorie SET NOT NULL;

ALTER SEQUENCE classe_categorie_id_classe_categorie_seq OWNED BY classe_categorie.id_classe_categorie;

CREATE SEQUENCE IF NOT EXISTS tarif_classe_trajet_id_tarif_classe_trajet_seq;
SELECT setval('tarif_classe_trajet_id_tarif_classe_trajet_seq', COALESCE((SELECT MAX(id_tarif_classe_trajet) FROM tarif_classe_trajet), 0));

ALTER TABLE tarif_classe_trajet
  ALTER COLUMN id_tarif_classe_trajet SET DEFAULT nextval('tarif_classe_trajet_id_tarif_classe_trajet_seq'),
  ALTER COLUMN id_tarif_classe_trajet SET NOT NULL;

ALTER SEQUENCE tarif_classe_trajet_id_tarif_classe_trajet_seq OWNED BY tarif_classe_trajet.id_tarif_classe_trajet;

-- Même chose pour l'autre table
CREATE SEQUENCE IF NOT EXISTS tarif_classe_categorie_trajet_id_tarif_classe_categorie_trajet_seq;
SELECT setval('tarif_classe_categorie_trajet_id_tarif_classe_categorie_trajet_seq', COALESCE((SELECT MAX(id_tarif_classe_categorie_trajet) FROM tarif_classe_categorie_trajet), 0));

ALTER TABLE tarif_classe_categorie_trajet
  ALTER COLUMN id_tarif_classe_categorie_trajet SET DEFAULT nextval('tarif_classe_categorie_trajet_id_tarif_classe_categorie_trajet_seq'),
  ALTER COLUMN id_tarif_classe_categorie_trajet SET NOT NULL;

ALTER SEQUENCE tarif_classe_categorie_trajet_id_tarif_classe_categorie_trajet_seq OWNED BY tarif_classe_categorie_trajet.id_tarif_classe_categorie_trajet;
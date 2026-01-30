-- Migration des entités PaiementDiffusion et PaiementDetails

-- 1. Modifier la table paiement_diffusion pour référencer diffusion_publicitaire
ALTER TABLE paiement_diffusion 
DROP FOREIGN KEY IF EXISTS FK_paiement_diffusion_societe;

ALTER TABLE paiement_diffusion 
DROP COLUMN IF EXISTS id_societe;

ALTER TABLE paiement_diffusion 
ADD COLUMN id_diffusion_pub INT;

ALTER TABLE paiement_diffusion 
ADD CONSTRAINT FK_paiement_diffusion_diffusion_pub 
FOREIGN KEY (id_diffusion_pub) REFERENCES diffusion_publicitaire(id_diffusion_publicitaire);

-- 2. Créer la table paiement_details
CREATE TABLE IF NOT EXISTS paiement_details (
    id_paiement_details INT AUTO_INCREMENT PRIMARY KEY,
    id_paiement INT NOT NULL,
    montant DECIMAL(15,2) NOT NULL,
    id_diffusion_details INT NOT NULL,
    date DATE,
    
    CONSTRAINT FK_paiement_details_paiement 
        FOREIGN KEY (id_paiement) REFERENCES paiement_diffusion(id_paiement_diffusion),
    CONSTRAINT FK_paiement_details_diffusion 
        FOREIGN KEY (id_diffusion_details) REFERENCES diffusion_pub_details(id_diffusion_pub_details)
);

-- 3. Ajouter des index pour les performances
CREATE INDEX idx_paiement_details_paiement ON paiement_details(id_paiement);
CREATE INDEX idx_paiement_details_diffusion ON paiement_details(id_diffusion_details);
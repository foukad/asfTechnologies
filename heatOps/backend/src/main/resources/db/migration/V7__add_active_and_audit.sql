-- Ajout du statut de l'intervention
ALTER TABLE intervention
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'PLANNED';

-- Ajout des timestamps métier (optionnels mais recommandés)
ALTER TABLE intervention
    ADD COLUMN started_at TIMESTAMP NULL,
    ADD COLUMN completed_at TIMESTAMP NULL;

-- Contrainte de validation des statuts
ALTER TABLE intervention
    ADD CONSTRAINT chk_intervention_status
        CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'DONE'));

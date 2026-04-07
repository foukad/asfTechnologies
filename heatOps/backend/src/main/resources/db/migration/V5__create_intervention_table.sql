CREATE TABLE intervention (
                              id UUID PRIMARY KEY,
                              company_id UUID NOT NULL,

                              title VARCHAR(255),
                              description TEXT,
                              scheduled_at TIMESTAMP,
                              address VARCHAR(255),
                              city VARCHAR(255),
                              postal_code VARCHAR(20),
                              technician_name VARCHAR(255),

                              CONSTRAINT fk_intervention_company
                                  FOREIGN KEY (company_id)
                                      REFERENCES company(id)
);


ALTER TABLE intervention ADD COLUMN client_id UUID;
ALTER TABLE intervention
    ADD CONSTRAINT fk_intervention_client
        FOREIGN KEY (client_id) REFERENCES client(id);

ALTER TABLE intervention ADD COLUMN equipment_id UUID;
ALTER TABLE intervention
    ADD CONSTRAINT fk_intervention_equipment
        FOREIGN KEY (equipment_id) REFERENCES equipment(id);

ALTER TABLE intervention ADD COLUMN technician_id UUID;

ALTER TABLE intervention
    ADD CONSTRAINT fk_intervention_technician
        FOREIGN KEY (technician_id) REFERENCES technician(id);


CREATE TABLE equipment (
                           id UUID PRIMARY KEY,
                           company_id UUID NOT NULL,

                           name VARCHAR(255),
                           brand VARCHAR(255),
                           model VARCHAR(255),
                           serial_number VARCHAR(255),
                           year INT,

                           CONSTRAINT fk_equipment_company
                               FOREIGN KEY (company_id)
                                   REFERENCES company(id)
);


ALTER TABLE equipment ADD COLUMN client_id UUID;
ALTER TABLE equipment
    ADD CONSTRAINT fk_equipment_client
        FOREIGN KEY (client_id) REFERENCES client(id);

CREATE TABLE technician (
                            id UUID PRIMARY KEY,
                            company_id UUID NOT NULL,

                            first_name VARCHAR(255),
                            last_name VARCHAR(255),
                            phone VARCHAR(50),
                            email VARCHAR(255),

                            CONSTRAINT fk_technician_company
                                FOREIGN KEY (company_id)
                                    REFERENCES company(id)
);

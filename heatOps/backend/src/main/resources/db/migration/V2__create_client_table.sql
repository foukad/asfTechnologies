CREATE TABLE client (
                        id UUID PRIMARY KEY,
                        company_id UUID NOT NULL,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        phone VARCHAR(50),
                        email VARCHAR(255),
                        address VARCHAR(255),
                        city VARCHAR(255),
                        postal_code VARCHAR(20),

                        CONSTRAINT fk_client_company
                            FOREIGN KEY (company_id)
                                REFERENCES company(id)
);

CREATE TABLE company (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL
);

CREATE TABLE app_user (
                          id UUID PRIMARY KEY,
                          company_id UUID NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(50) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          FOREIGN KEY (company_id) REFERENCES company(id)
);

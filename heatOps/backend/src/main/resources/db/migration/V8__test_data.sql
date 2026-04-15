-- Tenant unique pour les tests
INSERT INTO company (id, name, created_at) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Test Company', NOW())
    ON CONFLICT DO NOTHING;

-- Clients
INSERT INTO client (id, company_id, first_name, last_name, email, phone, active, created_at, updated_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'Client Alpha', 'alpha55','alpha@test.com', '0600000001', true, NOW(), NOW()),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'Client Beta', 'Beta66','beta@test.com', '0600000002', true, NOW(), NOW());

-- Technicians
INSERT INTO technician (id, company_id, first_name, last_name, phone, active, created_at, updated_at)
VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '11111111-1111-1111-1111-111111111111', 'John', 'Doe', '0700000001', true, NOW(), NOW());

-- Equipments
INSERT INTO equipment (id, company_id, name, brand, model, serial_number, active, created_at, updated_at)
VALUES
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', '11111111-1111-1111-1111-111111111111', 'boiler', 'Vaillant', 'EcoPlus', 'SN-BOILER-0001', true, NOW(), NOW());

-- Interventions
INSERT INTO intervention (id, company_id, title, description, status, scheduled_at, client_id, equipment_id, technician_id, active, created_at, updated_at)
VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '11111111-1111-1111-1111-111111111111',
     'Test Intervention', 'Initial test', 'PLANNED', NOW() + INTERVAL '1 day',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'dddddddd-dddd-dddd-dddd-dddddddddddd',
     'cccccccc-cccc-cccc-cccc-cccccccccccc',
     true, NOW(), NOW());

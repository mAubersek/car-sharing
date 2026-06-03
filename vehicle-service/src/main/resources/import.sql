INSERT INTO vehicles (id, brand, model, licensePlate, location, pricePerHour, status, createdAt)
VALUES
    (1, 'Toyota',  'Corolla', 'LJ-1234', 'Ljubljana', 5.00,  'AVAILABLE', NOW()),
    (2, 'BMW',     'X3',      'LJ-5678', 'Maribor',   12.00, 'AVAILABLE', NOW()),
    (3, 'Renault', 'Clio',    'LJ-9012', 'Celje',     4.00,  'AVAILABLE', NOW()),
    (4, 'Tesla',   'Model 3', 'LJ-3456', 'Ljubljana', 15.00, 'AVAILABLE', NOW());

ALTER SEQUENCE vehicles_SEQ RESTART WITH 100;
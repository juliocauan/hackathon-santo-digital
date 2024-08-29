CREATE TABLE IF NOT EXISTS product
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    colour VARCHAR(60)
);

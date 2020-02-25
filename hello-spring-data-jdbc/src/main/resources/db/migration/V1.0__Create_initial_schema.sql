CREATE TABLE accounts
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(64),
    last_name     VARCHAR(64),
    email         VARCHAR(64) UNIQUE,
    birthday      DATE,
    creation_time TIMESTAMP,
    balance       DECIMAL(11, 2)
);



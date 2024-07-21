--liquibase formatted sql

--changeset didenko:1
CREATE TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    UUID     VARCHAR NOT NULL UNIQUE,
    role     VARCHAR NOT NULL,
    username VARCHAR NOT NULL UNIQUE,
    email    VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
)
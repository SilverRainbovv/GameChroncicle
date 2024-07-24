--liquibase formatted sql

--changeset didenko:1
CREATE TABLE user_list(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL REFERENCES users(id),
    UID VARCHAR NOT NULL UNIQUE ,
    name VARCHAR NOT NULL ,
    type VARCHAR NOT NULL
);

--changeset didenko:2
CREATE TABLE list_game(
    list_id BIGINT NOT NULL UNIQUE ,
    igdb_game_id BIGINT NOT NULL
);
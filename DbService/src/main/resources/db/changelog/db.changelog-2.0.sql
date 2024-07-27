--liquibase formatted sql

--changeset didenko:1
CREATE TABLE gamelist(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL REFERENCES users(id),
    UID VARCHAR NOT NULL UNIQUE ,
    name VARCHAR NOT NULL ,
    type VARCHAR NOT NULL
);

--changeset didenko:2
CREATE TABLE game(
    id BIGSERIAL PRIMARY KEY ,
    igdb_id BIGINT NOT NULL
);

--changeset didenko:3
CREATE TABLE gamelist_game(
    list_id BIGINT NOT NULL REFERENCES gamelist(id),
    game_id BIGINT NOT NULL REFERENCES game(id),
    PRIMARY KEY (list_id, game_id)
);
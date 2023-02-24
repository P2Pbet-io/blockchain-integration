--liquibase formatted sql

--changeset anisimov:1875181 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Инициализация системного кошелька

CREATE TABLE system_wallet
(
    address       VARCHAR     NOT NULL,
    type          VARCHAR     NOT NULL,
    nonce         NUMERIC     NOT NULL DEFAULT 0,
    CONSTRAINT pk_ethereum_wallet PRIMARY KEY (address)
);
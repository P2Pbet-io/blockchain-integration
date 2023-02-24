--liquibase formatted sql

--changeset anisimov:1875185 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Инициализация сущности управляющего кошелька


CREATE TABLE governance_wallet
(
    address          VARCHAR   NOT NULL,
    nonce            BIGINT    NOT NULL,
    balance          NUMERIC   NOT NULL,
    type             VARCHAR   NOT NULL,
    control_contract VARCHAR   NOT NULL,
    created_date     TIMESTAMP NOT NULL,
    modified_date    TIMESTAMP NOT NULL,
    CONSTRAINT pk_governance_wallet PRIMARY KEY (address)
);
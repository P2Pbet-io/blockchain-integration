--liquibase formatted sql

--changeset anisimov:1875183 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Инициализация сущности логов и синхронизации

CREATE TABLE log_sc
(
    id UUID NOT NULL,
    contract_address VARCHAR   NOT NULL,
    block_number     BIGINT    NOT NULL,
    transaction_hash VARCHAR   NOT NULL,
    log_type         VARCHAR   NOT NULL,
    data             JSON      NOT NULL,
    created_date     TIMESTAMP NOT NULL,
    CONSTRAINT pk_log_sc PRIMARY KEY (id)
);

CREATE TABLE log_sync
(
    contract_address VARCHAR   NOT NULL,
    name             VARCHAR   NOT NULL,
    last_block_sync  BIGINT    NOT NULL,
    created_date     TIMESTAMP NOT NULL,
    modified_date    TIMESTAMP NOT NULL,
    CONSTRAINT pk_log_sync PRIMARY KEY (contract_address)
);
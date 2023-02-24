--liquibase formatted sql

--changeset anisimov:1875184 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Добавление уникального хэша в log_sc

ALTER TABLE log_sc
    ADD COLUMN sync_hash VARCHAR NOT NULL;

ALTER TABLE log_sc
    ADD CONSTRAINT uk_log_sc_sync_hash UNIQUE (sync_hash);

CREATE INDEX idx_log_sc_sync_hash
    ON log_sc (sync_hash);
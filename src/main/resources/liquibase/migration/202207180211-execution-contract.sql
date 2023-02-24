--liquibase formatted sql

--changeset anisimov:1875186 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Инициализация сущности исполнения операций


CREATE TABLE execution_contract
(
    id UUID NOT NULL,
    log_type           VARCHAR   NOT NULL,
    nonce              BIGINT    NOT NULL,
    governance_address VARCHAR   NOT NULL,
    contract_address   VARCHAR   NOT NULL,
    gas_limit          BIGINT    NOT NULL,
    gas_price          BIGINT    NOT NULL,
    fee                NUMERIC   NOT NULL,
    data               JSON      NOT NULL,
    encoded_tx_data    VARCHAR   NOT NULL,
    signed_tx_data     VARCHAR   NOT NULL,
    transaction_hash   VARCHAR   NOT NULL,
    error_message      VARCHAR   NOT NULL,
    execution_status   VARCHAR   NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    modified_date      TIMESTAMP NOT NULL,
    CONSTRAINT pk_execution_contract PRIMARY KEY (id)
);
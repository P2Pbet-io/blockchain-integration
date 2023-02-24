--liquibase formatted sql

--changeset anisimov:1875187 splitStatements:false runOnChange:false logicalFilePath:1.sql
--comment Удаление лишних not null констрейнтов


alter table execution_contract
    alter column governance_address drop not null;
alter table execution_contract
    alter column contract_address drop not null;
alter table execution_contract
    alter column gas_limit drop not null;
alter table execution_contract
    alter column gas_price drop not null;
alter table execution_contract
    alter column fee drop not null;
alter table execution_contract
    alter column signed_tx_data drop not null;
alter table execution_contract
    alter column transaction_hash drop not null;
alter table execution_contract
    alter column error_message drop not null;
alter table execution_contract
    alter column nonce drop not null;

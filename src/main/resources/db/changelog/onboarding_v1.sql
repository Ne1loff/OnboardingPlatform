--liquibase formatted sql localFilePath:db/changelog/onboarding_v1.sql
--changeset sergey_zhilkin:add_file_storage_table

CREATE TABLE onboarding_documents
(
    key      UUID         NOT NULL PRIMARY KEY,
    filename VARCHAR(512) NOT NULL
);

--changeset sergey_zhilkin:hrs_table

CREATE TABLE onboarding_hrs
(
    chat_id    BIGINT       NOT NULL PRIMARY KEY,
    username   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

--changeset sergey_zhilkin:update_scenario_route_definition_add_id

ALTER TABLE scenario_route_definition
    ADD COLUMN id uuid;

UPDATE scenario_route_definition
SET id = (select gen_random_uuid())
WHERE id IS NULL;

ALTER TABLE scenario_route_definition
    DROP CONSTRAINT scenario_route_definition_pkey;

ALTER TABLE scenario_route_definition
    ALTER COLUMN id SET NOT NULL,
    ADD PRIMARY KEY (id);

--changeset sergey_zhilkin:add_global_properties

CREATE TABLE onboarding_properties
(
    name        VARCHAR(255) PRIMARY KEY NOT NULL,
    description VARCHAR(1024),
    value       VARCHAR(1024)            NOT NULL
);

--changeset sergey_zhilkin:update_scenario_route_definition_add_status

ALTER TABLE scenario_route_definition
    ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'DRAFT';

--changeset sergey_zhilkin:add_notification

CREATE TABLE scenario_notification
(
    id             UUID         NOT NULL PRIMARY KEY,
    chat_id        BIGINT       NOT NULL,
    message        TEXT         NOT NULL,
    repeat         BOOLEAN      NOT NULL,
    delay          VARCHAR(255) NOT NULL,
    status         VARCHAR(255) NOT NULL,
    next_execution TIMESTAMPTZ  NOT NULL
)
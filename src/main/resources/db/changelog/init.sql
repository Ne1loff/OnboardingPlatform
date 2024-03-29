--liquibase formatted sql localFilePath:db/changelog/init.sql
--changeset sergey_zhilkin:init_scenarios_schema

CREATE TABLE scenario_route_definition
(
    scenario_name   VARCHAR(255) NOT NULL PRIMARY KEY,
    first_action_id UUID         NOT NULL,
    route_source    JSONB        NOT NULL,
    matcher         JSONB        NOT NULL
);

CREATE TABLE scenario
(
    id                UUID         NOT NULL PRIMARY KEY,
    scenario_name     VARCHAR(255) NOT NULL,
    chat_id           BIGINT       NOT NULL,
    current_action_id UUID         NOT NULL,
    is_active         BOOLEAN      NOT NULL
)

--changeset sergey_zhilkin:add_first_action_id_column_to_scenarios_table

ALTER TABLE scenario
    ADD COLUMN first_action_id UUID;

UPDATE scenario
SET first_action_id = srd.first_action_id
    FROM (SELECT first_action_id FROM scenario_route_definition srd where srd.first_action_id = first_action_id) as srd;

ALTER TABLE scenario
    ALTER COLUMN first_action_id SET NOT NULL;

--changeset sergey_zhilkin:add_context_column

ALTER TABLE scenario
    ADD COLUMN context JSONB;

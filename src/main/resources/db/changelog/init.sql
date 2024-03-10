--liquibase formatted sql localFilePath:db/changelog/init.sql
--changeset sergey_zhilkin:init_scenarios_schema

CREATE TABLE scenario_route_definition
(
    scenario_name varchar(255) not null primary key,
    route_source  jsonb        not null,
    matcher       jsonb        not null
);

CREATE TABLE scenario
(
    id                uuid         not null primary key,
    scenario_name     varchar(255) not null,
    chat_id           integer      not null,
    current_action_id uuid         not null,
    is_active         boolean      not null
)
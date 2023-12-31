--liquibase formatted sql

--changeset leto:61
CREATE TABLE user_tg
(
    id                BIGINT generated by default as identity primary key,
    first_name        TEXT not null,
    took_a_pet        bool,
    date_time_to_took TIMESTAMP,
    chat_id           int  not null,
    telephone_number  TEXT
);

CREATE TABLE report_tg
(
    id                 BIGINT generated by default as identity primary key,
    date_added         TIMESTAMP,
    general_well_being TEXT,
    photo_name         TEXT,
    user_id            BIGINT references user_tg (id) on delete set null,
    check_report       bool
);

CREATE TABLE volunteers
(
    id_volunteer      BIGINT generated by default as identity primary key,
   name_volunteer     TEXT   not null,
   last_name_volunteer     TEXT   not null,
    chat_id_volunteer BIGINT   not null

);

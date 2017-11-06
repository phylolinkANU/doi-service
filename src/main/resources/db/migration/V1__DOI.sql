CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;
CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;

/* Hibernate likes to assign ids before actually inserting things, so create a sequence for it */
CREATE SEQUENCE hibernate_sequence
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

CREATE TABLE IF NOT EXISTS doi (
  id bigint NOT NULL PRIMARY KEY,
  uuid uuid NOT NULL DEFAULT gen_random_uuid() UNIQUE,
  doi CITEXT NOT NULL UNIQUE,
  title text NOT NULL,
  authors text NOT NULL,
  description text NOT NULL,
  date_minted timestamp without time zone NOT NULL,
  provider text NOT NULL,
  filename text NOT NULL,
  content_type text NOT NULL,
  user_id text,

  provider_metadata jsonb NOT NULL,
  application_metadata jsonb,

  custom_landing_page_url text,
  application_url text,

  version bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL
);

CREATE INDEX IF NOT EXISTS doi_uuid_idx on doi (uuid);
CREATE INDEX IF NOT EXISTS doi_doi_idx on doi (doi);
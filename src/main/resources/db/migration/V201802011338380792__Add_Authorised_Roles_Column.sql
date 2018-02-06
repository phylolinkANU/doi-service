/*
Add as-idempotent-as-possible DDL statements here
See: http://www.jeremyjarrell.com/using-flyway-db-with-distributed-version-control/
*/

ALTER TABLE doi ADD COLUMN IF NOT EXISTS authorised_roles text ARRAY DEFAULT null;

COMMENT ON COLUMN doi.authorised_roles IS 'What roles a user needs to access the file for this doi if not the owner';
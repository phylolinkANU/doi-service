/*
Add as-idempotent-as-possible DDL statements here
See: http://www.jeremyjarrell.com/using-flyway-db-with-distributed-version-control/
*/

ALTER TABLE doi ADD COLUMN IF NOT EXISTS active boolean NOT NULL DEFAULT true;
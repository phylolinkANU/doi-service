/*
Add as-idempotent-as-possible DDL statements here
See: http://www.jeremyjarrell.com/using-flyway-db-with-distributed-version-control/
*/

ALTER TABLE doi ADD COLUMN IF NOT EXISTS display_template text DEFAULT null;

COMMENT ON COLUMN doi.display_template IS 'If specified use this template instead of the default when rendering the DOI display page';

UPDATE doi SET display_template = 'biocache' WHERE application_url <>'http://base.ala.org.au/datacheck/datasets';


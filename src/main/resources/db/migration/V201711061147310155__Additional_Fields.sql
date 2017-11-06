ALTER TABLE doi
  ADD COLUMN IF NOT EXISTS file_hash bytea,
  ADD COLUMN IF NOT EXISTS file_size bigint,
  ADD COLUMN IF NOT EXISTS licence text;
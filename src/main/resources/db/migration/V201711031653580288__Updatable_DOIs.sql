ALTER TABLE doi ADD COLUMN user_id text;
ALTER TABLE doi
  ALTER filename DROP NOT NULL,
  ALTER content_type DROP NOT NULL;
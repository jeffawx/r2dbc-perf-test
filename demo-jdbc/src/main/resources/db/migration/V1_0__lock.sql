-- ********** OPTIMISTIC LOCKING **********

CREATE OR REPLACE FUNCTION update_version() RETURNS trigger AS
$update_version$
BEGIN
    IF NEW.VERSION IS NULL THEN
        NEW.VERSION := 0;
    ELSIF OLD.VERSION IS DISTINCT FROM NEW.VERSION THEN
        RAISE EXCEPTION 'Version has changed';
    ELSE
        NEW.VERSION := OLD.VERSION + 1;
    END IF;
    NEW.LAST_UPDATE := NOW();
    RETURN NEW;
END
$update_version$ LANGUAGE plpgsql;

-- ********** UPDATE CREATED TIME **********

CREATE OR REPLACE FUNCTION update_created_at() RETURNS trigger AS
$update_created_at$
BEGIN
    IF NEW.VERSION IS NULL THEN
        NEW.VERSION := 0;
    END IF;
    IF NEW.CREATE_TIME IS NULL THEN
        NEW.CREATE_TIME := NOW();
    END IF;
    NEW.LAST_UPDATE := NEW.CREATE_TIME;
    RETURN NEW;
END
$update_created_at$ LANGUAGE plpgsql;

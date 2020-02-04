CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customer
(
    id          uuid        NOT NULL PRIMARY KEY DEFAULT uuid_generate_v1(),
    name        text        NOT NULL,
    email       text,
    address     text,
    create_time timestamptz NOT NULL             DEFAULT now(),
    last_update timestamptz NOT NULL             DEFAULT now()
);

CREATE TABLE orders
(
    id          UUID      NOT NULL PRIMARY KEY,
    create_time TIMESTAMP NOT NULL DEFAULT now(),
    last_update TIMESTAMP NOT NULL DEFAULT now(),
    data        JSONB     NOT NULL,
    version     INTEGER   NOT NULL DEFAULT 0
);

CREATE TRIGGER update_customer_version
    BEFORE UPDATE
    ON customer
    FOR EACH ROW
EXECUTE PROCEDURE update_version();
CREATE TRIGGER update_customer_created_at
    BEFORE INSERT
    ON customer
    FOR EACH ROW
EXECUTE PROCEDURE update_created_at();

CREATE TRIGGER update_order_version
    BEFORE UPDATE
    ON orders
    FOR EACH ROW
EXECUTE PROCEDURE update_version();
CREATE TRIGGER update_order_created_at
    BEFORE INSERT
    ON orders
    FOR EACH ROW
EXECUTE PROCEDURE update_created_at();

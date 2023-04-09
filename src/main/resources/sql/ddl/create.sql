CREATE SCHEMA IF NOT EXISTS market;

CREATE SEQUENCE IF NOT EXISTS market.product_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS market.discount_card_seq
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    INCREMENT BY 1
    CACHE 1;

CREATE TABLE IF NOT EXISTS market.product (
    id              INTEGER         PRIMARY KEY     DEFAULT nextval('market.product_seq'),
    description     VARCHAR(20)     NOT NULL,
    price           DECIMAL         NOT NULL,
    quantity        INTEGER         NOT NULL,
    is_discount     BOOLEAN         NOT NULL        CHECK (quantity >=0)
);

CREATE TABLE IF NOT EXISTS market.discount_card (
    id              INTEGER         PRIMARY KEY     DEFAULT nextval('market.discount_card_seq'),
    discount        SMALLINT	    NOT NULL        CHECK (discount BETWEEN 0 AND 100)
);

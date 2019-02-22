CREATE TABLE invoices
(
  id                SERIAL                  PRIMARY KEY,
  order_id          BIGINT                  NOT NULL,
  invoice_number    VARCHAR(64)             NOT NULL,
  created_at        NUMERIC                 NOT NULL
);

CREATE INDEX idx_order_id ON invoices(order_id);
CREATE INDEX idx_invoice_number ON invoices(invoice_number);

CREATE INDEX idx_customer_id ON orders ((data ->> 'customerId'));

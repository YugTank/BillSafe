CREATE TABLE purchase.purchases(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    product_name varchar(255) NOT NULL,
    category varchar(255) NOT NULL,
    brand varchar(250),
    purchase_date DATE NOT NULL,
    warranty_month INT NOT NULL,
    warranty_expiry DATE NOT NULL,
    store varchar(250),
    price DECIMAL(10,2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_purchase_user FOREIGN KEY (user_id) REFERENCES auth.users(id)
)
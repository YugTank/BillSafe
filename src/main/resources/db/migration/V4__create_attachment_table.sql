CREATE TABLE purchase.attachments(
    id UUID PRIMARY KEY,
    purchase_id UUID NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attachment_purchase
        FOREIGN KEY (purchase_id)
        REFERENCES purchase.purchases(id)
        ON DELETE CASCADE
)
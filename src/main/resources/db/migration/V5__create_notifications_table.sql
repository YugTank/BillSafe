
CREATE TABLE notification.notifications(
    id UUID PRIMARY KEY,
    purchase_id UUID NOT NULL,
    user_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    reminder_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);